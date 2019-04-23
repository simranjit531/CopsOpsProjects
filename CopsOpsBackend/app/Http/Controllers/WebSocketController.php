<?php
namespace App\Http\Controllers;

use Ratchet\MessageComponentInterface;
use Ratchet\ConnectionInterface;
use App\Chat;
use App\User;
use App\UserType;
use Carbon\Carbon;
use App\UserDeviceMapping;

class WebSocketController implements MessageComponentInterface
{
    protected $clients;

    private $subscriptions;

    private $users;

    private $userresources;

    public function __construct()
    {
        $this->clients = new \SplObjectStorage();
        $this->subscriptions = [];
        $this->users = [];
        $this->userresources = [];
    }

    /**
     * [onOpen description]
     *
     * @method onOpen
     * @param ConnectionInterface $conn
     *            [description]
     * @return [JSON] [description]
     * @example connection var conn = new WebSocket('ws://localhost:8090');
     */
    public function onOpen(ConnectionInterface $conn)
    {
        $this->clients->attach($conn);
        $this->users[$conn->resourceId] = $conn;
    }

    /**
     * [onMessage description]
     *
     * @method onMessage
     * @param ConnectionInterface $conn
     *            [description]
     * @param [JSON.stringify] $msg
     *            [description]
     * @return [JSON] [description]     
     * 
     * @example message conn.send(JSON.stringify({user:"{username:"Test", id: 1}", type: "message", to_user: "1", message: "it needs xss protection", message_type: "TEXT|PDF|IMAGE"}));
     * @example register conn.send(JSON.stringify({user:"{username:"Test", id: 1}, type: "register"}));
     * @example userlist conn.send(JSON.stringify({user:"{username:"Test", id: 1}, type: "userlist"}));
     * @example recentchats conn.send(JSON.stringify({user:"{username:"Test", id: 1}, type: "recentchat"}));
     * @example chathistory conn.send(JSON.stringify({user:"{username:"Test", id: 1}, type: "chathistory", to_user : "1"}));
     * 
     * 
     */
    public function onMessage(ConnectionInterface $conn, $msg)
    {
        file_put_contents(public_path('uploads/text.txt'), $msg);
        $data = json_decode($msg);
        if (isset($data->type)) 
        {
            switch ($data->type) 
            {
                case "message":
                    
                    # Store chat here
                    Chat::create([
                        'sender_id' => $data->user->id, 
                        'receiver_id' => $data->to_user, 
                        'message' => $data->message, 
                        'message_type' => isset($data->message_type) ? $data->message_type : "TEXT"
                    ]);
                    
                    $data->to_username = User::find($data->user->id)->first_name.' '.User::find($data->user->id)->last_name;
                    $msg = json_encode($data);
                    
                    # Validate if to user is cop
                    $userData = User::find($data->to_user);
                    if(!$userData->isEmpty())
                    {
                        if($userData->ref_user_type_id == UserType::_TYPE_OPERATOR) 
                        {
                            $tokenData = UserDeviceMapping::select('device_token')->where('ref_user_id', $data->to_user)->get();
                            
                            if(!$tokenData->isEmpty())
                            {
                                $push = new \Edujugon\PushNotification\PushNotification('fcm');
                                $push->setMessage([
                                    'notification' => [
                                        'title'=>'You have received a new message',
                                        'body'=>'You have received a new message',
                                        'sound' => 'default'
                                    ]
                                ]);
                                
                                $push->setDevicesToken($tokenData[0]->device_token);
                                $push->send();
                                $push->getFeedback();
                            }
                        }
                    }
                    
                    # Once new incident is registered, send push notification
                                          
                    if (isset($this->userresources[$data->to_user])) 
                    {
                        foreach ($this->userresources[$data->to_user] as $key => $resourceId) 
                        {
                            if (isset($this->users[$resourceId])) 
                            {
                                $this->users[$resourceId]->send($msg);                        
                            }
                        }
                        $conn->send(json_encode($this->userresources[$data->to_user]));
                    }
                    if (isset($this->userresources[$data->user->id])) 
                    {
                        foreach ($this->userresources[$data->user->id] as $key => $resourceId) 
                        {
                            if (isset($this->users[$resourceId]) && $conn->resourceId != $resourceId) 
                            {
                                $this->users[$resourceId]->send($msg);                            
                            }
                        }
                    }
                    break;
                    
                case "register":
                    
                    $totalMessageCount = 0;
                    if (isset($data->user->id)) 
                    {
                        if (isset($this->userresources[$data->user->id])) 
                        {
                            if (! in_array($conn->resourceId, $this->userresources[$data->user->id])) 
                            {
                                $this->userresources[$data->user->id][] = $conn->resourceId;
                            }
                        }
                        else 
                        {
                            $this->userresources[$data->user->id] = [];
                            $this->userresources[$data->user->id][] = $conn->resourceId;
                        }
                                                
                        # Get Unread message count
                        $totalMessageCount = Chat::select('id')
                        ->where(function($query) use ($data){
//                             $query->where(['sender_id'=>$data->user->id]);
                            $query->where(['receiver_id'=>$data->user->id]);
                        })
                        ->where('is_read', 0)
                        ->get();
                        
                        $this->users['totalMessageCount'] = count($totalMessageCount);
                    }
                    
                    $conn->send(json_encode($this->users));
                    $conn->send(json_encode($this->userresources));
                    break;
                    
                case "userlist":
                    
                    if(isset($data->user->id))
                    {
                        $userList = User::where('id', '<>', $data->user->id)->
                        whereIn('ref_user_type_id', [UserType::_TYPE_BACKOFFICE, UserType::_TYPE_OPERATOR]);
                        if(isset($data->term) && !empty($data->term))
                        {
                            $userList = $userList->where('first_name', 'like', '%'.$data->term.'%');
                            $userList = $userList->orWhere('last_name', 'like', '%'.$data->term.'%');
                        }
                        $userList = $userList->get();
                        
                        $users = array();
                        if(!$userList->isEmpty())
                        {
                            foreach ($userList as $user)
                            {
                                $username = ucwords(strtolower($user->first_name.' '.$user->last_name));
                                array_push($users, array('id'=>$user->id, 'username'=>$username));
                            }
                        }
                        
                        # Get Unread message count
                        $totalMessageCount = Chat::select('id')
                        ->where(function($query) use ($data){
//                             $query->where(['sender_id'=>$data->user->id]);
                            $query->where(['receiver_id'=>$data->user->id]);
                        })
                        ->where('is_read', 0)
                        ->get();
                                              
                        
                        $package = [
                            'users' => $users,
                            'type' => 'userlist',
                            'totalMessageCount' => count($totalMessageCount)
                        ];
                        
                        $conn->send(json_encode($package));
                        
                    }
                    
                    break;
                
                case "recentchats":
                    
                    if(isset($data->user->id))
                    {
                        $query = "SELECT t1.* FROM copops_chat AS t1
                                    INNER JOIN
                                    (
                                        SELECT LEAST(sender_id, receiver_id) AS sender_id, GREATEST(sender_id, receiver_id) AS receiver_id, MAX(id) AS max_id
                                        FROM copops_chat GROUP BY LEAST(sender_id, receiver_id), GREATEST(sender_id, receiver_id)
                                    ) AS t2
                                    ON LEAST(t1.sender_id, t1.receiver_id) = t2.sender_id AND GREATEST(t1.sender_id, t1.receiver_id) = t2.receiver_id AND t1.id = t2.max_id
                                    WHERE t1.sender_id = ? OR t1.receiver_id = ?";  
                                                                        
                        $recentchats = \DB::select($query, [$data->user->id, $data->user->id]);
                        
                        if(!empty($recentchats))
                        {
                            foreach ($recentchats as $c)
                            {
                                /* Get unread message count */
                                $unreadCount = Chat::where(function($query) use ($c, $data){
                                    $query->where(['sender_id'=>$c->sender_id, 'receiver_id'=>$data->user->id]);
                                })
                                ->orWhere(function($query) use ($c, $data){
                                    $query->where(['sender_id'=>$c->receiver_id, 'receiver_id'=>$data->user->id]);
                                })
                                ->where('is_read', 0)
                                ->get();
                                
                                
                                if($c->sender_id == $data->user->id)
                                {
                                    $c->user_id = $c->receiver_id;
                                    $c->user = User::find($c->receiver_id)->first_name.' '.User::find($c->receiver_id)->last_name;
                                }
                                else {
                                    $c->user_id = $c->sender_id;
                                    $c->user = User::find($c->sender_id)->first_name.' '.User::find($c->sender_id)->last_name;
                                }
                                
                                $c->time = Carbon::parse($c->created_at)->diffForHumans();
                                $c->unread = count($unreadCount);
                            }
                        }
                        
                        # Get Unread message count
                        $totalMessageCount = Chat::select('id')
                        ->where(function($query) use ($data){
//                             $query->where(['sender_id'=>$data->user->id]);
                            $query->where(['receiver_id'=>$data->user->id]);
                        })
                        ->where('is_read', 0)
                        ->get();
                        
                        $package = [
                            'recentchat' => $recentchats,
                            'type' => 'recentchats',
                            'totalMessageCount' => count($totalMessageCount)
                        ];
                        
                        $conn->send(json_encode($package));
                        
                    }                    
                    
                    break;
                    
               
                case "chathistory":
                    
                    if(isset($data->user->id))
                    {
                        $chat = Chat::where(function($query) use ($data){
                            $query->where(['sender_id'=>$data->user->id, 'receiver_id'=>$data->to_user]);
                        })
                        ->orWhere(function($query) use ($data){
                            $query->where(['sender_id'=>$data->to_user, 'receiver_id'=>$data->user->id]);
                        })
                        ->orderBy('created_at', 'desc')
//                         ->limit(10)
                        ->get();
                        
                        $chatArray = array();
                        if(!$chat->isEmpty())
                        {
                            foreach($chat as $c)
                            {
                                $output = array();
                                $output['message_id'] = $c->id;
                                $output['message'] = $c->message;
                                $output['message_type'] = $c->message_type;
                                
                                if($c->sender_id == $data->user->id)
                                {
                                    $output['sender'] = "ME";
                                    $output['receiver'] = User::find($c->receiver_id)->first_name.' '.User::find($c->receiver_id)->last_name;
                                }
                                else {
                                    $output['receiver'] = "ME";
                                    $output['sender'] = User::find($c->sender_id)->first_name.' '.User::find($c->sender_id)->last_name;
                                }
                                
                                array_push($chatArray, $output);
                            }
                        }
                        
                        # Get Unread message count
                        $totalMessageCount = Chat::select('id')
                        ->where(function($query) use ($data){
//                             $query->where(['sender_id'=>$data->user->id]);
                            $query->where(['receiver_id'=>$data->user->id]);
                        })
                        ->where('is_read', 0)
                        ->get();
                        
                        $new_package = [
                            'message' => array_reverse($chatArray),
                            'type' => 'chathistory',
                            'totalMessageCount' => count($totalMessageCount)
                        ];
                        $new_package = json_encode($new_package);
                        
                        $conn->send($new_package);                        
                        
                    }
                    
                    break;
                    
                case "seencount" : 
                    
                    if(isset($data->user->id))
                    {
                        /*
                        $chat = Chat::whereIn('id', [$data->seen])
                        ->update(['is_read'=>1]);
                        */
                        
                        $chat = Chat::where(['sender_id'=>$data->to_user, 'receiver_id' => $data->user->id]);                        
                        if(isset($data->seen))
                        {
                            $chat = $chat->whereIn('id', [$data->seen]);
                        }
                        $chat->update(['is_read'=>1]);
                        
                        # Get Unread message count
                        $totalMessageCount = Chat::select('id')
                        ->where(function($query) use ($data){
//                             $query->where(['sender_id'=>$data->user->id]);
                            $query->where(['receiver_id'=>$data->user->id]);
                        })
                        ->where('is_read', 0)
                        ->get();
                        
                        $new_package = [
                            'type' => 'seencount',
                            'totalMessageCount' => count($totalMessageCount)
                        ];
                        $new_package = json_encode($new_package);
                        
                        $conn->send($new_package);
                        
                    }
                    
                    break;
            }
        }
    }

    public function onClose(ConnectionInterface $conn)
    {
        $this->clients->detach($conn);
        echo "Connection {$conn->resourceId} has disconnected\n";
        unset($this->users[$conn->resourceId]);
        unset($this->subscriptions[$conn->resourceId]);
        foreach ($this->userresources as &$userId) {
            foreach ($userId as $key => $resourceId) {
                if ($resourceId == $conn->resourceId) {
                    unset($userId[$key]);
                }
            }
        }
    }

    public function onError(ConnectionInterface $conn, \Exception $e)
    {
        echo "An error has occurred: {$e->getMessage()}\n";
        $conn->close();
    }
    
    
}
