<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App;
use Datatables;
use Exception;
use App\User;
use Illuminate\Support\Collection;
use Illuminate\Support\Str;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;
use App\IncidentDetail;
use Carbon\Carbon;
use App\UserDeviceMapping;
use App\UserType;
use App\CopUserIncidentMapping;
use App\CopUserIncidentClosed;
use Illuminate\Support\Facades\Validator;
use Illuminate\Database\QueryException;
use App\Handrail;
use App\CopApprovalComments;
use App\CopUserIncidentTempMapping;
use App\ApplicationWaitNotification;
use DateTime;
use App\Crew;
use App\CrewUser;
use App\Util\ResponseMessage;
use App\Notification;
use Edujugon\PushNotification\Facades\PushNotification;
use Symfony\Component\HttpFoundation\StreamedResponse;
use function GuzzleHttp\json_encode;
use App\CopUserLocation;
use Session;
use Lang; 
use Intervention\Image\Facades\Image;

class BackendController extends Controller
{
	private $mimes = array('image/jpeg','image/jpg', 'image/png');
	private $extensions = array("jpeg","jpg","png","JPEG","JPG","PNG");
	
    public function __construct()
    {
        $this->middleware('auth');
    }

    public function index()
    {   
    	return view('backend.index');
    }

    public function usermanagement()
    {
        $operators = User::where(['ref_user_type_id' => UserType::_TYPE_OPERATOR, 'approved'=>1, 'available'=>1])->get(); //'available'=>1,'status'=>1

        $incidents = DB::table('cop_incident_details')->select('cop_incident_details.id', 'ref_incident_subcategory.sub_category_name',
            'cop_incident_details.incident_description')
            ->join('ref_incident_subcategory', 'ref_incident_subcategory.id', '=', 'cop_incident_details.ref_incident_subcategory_id')
            ->where('cop_incident_details.status', 1)
            ->get();

    	return view('backend.usermanagement')->with(['operators'=> $operators, 'incidents'=>$incidents]);
    }
	
	public function validationofregistrants()
	{
		return view('backend.validationofregistrants');
	}
	
	public function accountrefuses()
	{
		return view('backend.accountrefuses');
	}
	
	public function currenthand()
	{
		return view('backend.currenthand');
	}
	
	public function dailycrew()
    {
        $users = User::where([['ref_user_type_id','=',UserType::_TYPE_OPERATOR],['approved','=',1]])->get();
        $teams = DB::table('cop_crew')
                ->select('*')
                ->groupBy('created_at')
                ->orderBy('created_at', 'desc')
                ->get();
        
        $response = array();
        
        if(!$teams->isEmpty()){
            
            foreach ($teams as $k=>$t)
            {   
                $teams[$k]->members = Crew::find($t->id)->get_crew_members->toArray();
                $date = Carbon::parse($t->created_at)->format('l d/m/Y');
                
                $response[$date][] = $teams[$k];
            }
        }
        
        return view('backend.dailycrew', ['operators'=>$users, 'teams'=>$response]);
    }

    public function dailycrewfilter(Request $request)
    {
        try{
            $users = User::where([['ref_user_type_id','=',UserType::_TYPE_OPERATOR],['approved','=',1]])->get();
            
            $teams = DB::table('cop_crew')
            ->select('*');
            
            if($request->input('fromdatea') != "" && $request->input('todate') != "")
            {
                $fromdate = date_format( new DateTime($request->fromdatea), 'Y-m-d');
                $todate = date_format( new DateTime($request->todate), 'Y-m-d');
                
                $teams = $teams->whereBetween(DB::Raw("DATE_FORMAT(created_at, '%Y-%m-%d')"), [$fromdate, $todate]);
            }
            
            if($request->input('crewname') != "")
            {
                $crewname = $request->input('crewname');
                $teams = $teams->where('crew_name', 'like', '%'.$crewname.'%');
            }
                        
            $teams = $teams->groupBy('created_at')
            ->orderBy('created_at', 'desc')
            ->get();
            
            $response = array();
            
            if(!$teams->isEmpty())
            {                
                foreach ($teams as $k=>$t)
                {
                    $teams[$k]->members = Crew::find($t->id)->get_crew_members->toArray();
                    $date = Carbon::parse($t->created_at)->format('l d/m/Y');
                    
                    $response[$date][] = $teams[$k];
                }
            }
            
            return view('backend.dailycrew', ['operators'=>$users, 'teams'=>$response]);
            // return back()->with(['operators'=>$users, 'teams'=>$response]);
        }
        catch (QueryException $qe)
        {
            return redirect('dailycrew')->with(['type'=>'error', 'message'=>Lang::get("pages.OOPS")]);
        }
    }
	
	public function dailycrewdatefilter(Request $request)
	{
		 try{
			 $users = User::where([['ref_user_type_id','=',UserType::_TYPE_OPERATOR],['approved','=',1]])->get();
			 if($request->input('fromdatea') != "" && $request->input('todate') != "")
				{
					$fromdate = date_format( new DateTime($request->fromdatea), 'Y-m-d');		 
					$todate = date_format( new DateTime($request->todate), 'Y-m-d');	
					$teams = DB::table('cop_crew')
							->select('*')
							->whereBetween(DB::Raw("DATE_FORMAT(created_at, '%Y-%m-%d')"), [$fromdate, $todate])
							->groupBy('created_at')
							->orderBy('created_at', 'desc')
							->get();
				}
				else
				{
					$teams = DB::table('cop_crew')
							->select('*')
							->groupBy('created_at')
							->orderBy('created_at', 'desc')
							->get();
				}
				$response = array();
				
				if(!$teams->isEmpty()){
					
					foreach ($teams as $k=>$t)
					{   
						$teams[$k]->members = Crew::find($t->id)->get_crew_members->toArray();
						$date = Carbon::parse($t->created_at)->format('l d/m/Y');
						
						$response[$date][] = $teams[$k];
					}
				}
				
				return view('backend.dailycrew', ['operators'=>$users, 'teams'=>$response]);
		 } 
        catch (QueryException $qe) 
        {
            return redirect('dailycrew')->with(['type'=>'error', 'message'=>Lang::get("pages.OOPS")]);
        }
	}
	
	public function crewfilterget(Request $request)
	{
		
		 try{
			 $users = User::where([['ref_user_type_id','=',UserType::_TYPE_OPERATOR],['approved','=',1]])->get();
			 if($request->input('crewname') != "")
				{
					$crewname= $request->input('crewname');
					$teams = DB::table('cop_crew')
							->select('*')
							->where('crew_name','like', '%' . $crewname . '%')
							->groupBy('created_at')
							->orderBy('created_at', 'desc')
							->get();
				}
				else
				{
					$teams = DB::table('cop_crew')
							->select('*')
							->groupBy('created_at')
							->orderBy('created_at', 'desc')
							->get();
				}
				$response = array();
				
				if(!$teams->isEmpty()){
					
					foreach ($teams as $k=>$t)
					{   
						$teams[$k]->members = Crew::find($t->id)->get_crew_members->toArray();
						$date = Carbon::parse($t->created_at)->format('l d/m/Y');
						
						$response[$date][] = $teams[$k];
					}
				}
				return response()->json(['status'=>true, 'data'=>['operators'=>$users, 'teams'=>$response]]);
				
		 } 
        catch (QueryException $qe) 
        {
            return redirect('dailycrew')->with(['type'=>'error', 'message'=>Lang::get("pages.OOPS")]);
        }
	}
    
    public function dailycrewcreate(Request $request)
    {
        $userId = Auth::user()->id;
        
        $request->validate([
            'team_name'=>'required',
            'operator_name'=>'required',
            'fromdate'=>'required'
        ]);
        
        $date = Carbon::parse($request->input('fromdate'))->format('Y-m-d H:i:s');
        try {
            $crew = Crew::create([
                'crew_name'=>$request->input('team_name'),
                'incident_handrail'=>1,
                'cop_incident_details_id'=>1,
                'cop_handrail_id'=>1,
                'updated_by'=>$userId,
                'created_at'=>$date
            ]);
            
            if($crew->id)
            {
                foreach ($request->input('operator_name') as $o)
                {
                    CrewUser::create([
                        'cop_crew_id'=>$crew->id,
                        'ref_user_id'=>$o,
                        'updated_by'=>$userId
                    ]);
                }
            }
            
            return redirect('dailycrew')->with(['type'=>'success', 'message'=>Lang::get("pages.CrewCreatedSuccessfully")]);
        } 
        catch (QueryException $qe) 
        {
            return redirect('dailycrew')->with(['type'=>'error', 'message'=>Lang::get("pages.CrewCreatedSuccessfully")]);
        }
        
    }
    
    public function crewget(Request $request)
    {
        if($request->ajax())
        {
            if($request->has('crew_id'))
            {
                $response = array();
                $response['crew'] = Crew::find($request->input('crew_id'));
                $date = $response['crew']['created_at'];
                $date = Carbon::parse($date)->format('l d/m/Y');
                $response['crew']['date'] = $date; 
                $members = Crew::find($request->input('crew_id'))->get_crew_members->toArray();
                if(!empty($members))
                {
                    foreach ($members as $k=>$m)
                    {
                        $userData = User::find($m['ref_user_id']);
                        $members[$k]['member_name'] = $userData->first_name.' '.$userData->last_name;
                    }
                }
                $response['members'] = $members;
                
                return response()->json(['status'=>true, 'data'=>$response]);
            }
            return response()->json(['status'=>false, 'message'=>Lang::get("pages.Invalidrequest")]);
        }
        return response()->json(['status'=>false, 'message'=>Lang::get("pages.Invalidrequest")]);
    }
	
	public function reduseTabledata(Request $request)
	{
		 $users = User::where([['ref_user_type_id','=',UserType::_TYPE_OPERATOR],['approved','=',2]])->get();
		 return Datatables::of($users)->removeColumn('user_password')
		 ->addColumn('view', function($userview){
				return '<a href="javascript:void(0)" id="viewCops" rel="'.$userview->id.'" data-toggle="modal" data-target="#myModal"><i class="fa fa-angle-right" aria-hidden="true"></i></a>';
			})->rawColumns(array("view"))->make(true);
	}
	
	public function validationRegTabledata(Request $request)
	{
		$users = User::where([['ref_user_type_id','=',UserType::_TYPE_OPERATOR],['approved','=',0]])->get();
		 return Datatables::of($users)->removeColumn('user_password')
		 ->addColumn('view', function($userview){
				return '<a href="javascript:void(0)" id="viewCops" rel="'.$userview->id.'" data-toggle="modal" data-target="#myModal"><i class="fa fa-angle-right" aria-hidden="true"></i></a>';
			})->rawColumns(array("view"))->make(true);
	}
	
	public function archivedata(Request $request)
	{
		$users = \DB::table('cop_user_incidents_closed')->select('cop_user_incidents_closed.id','cop_incident_details.status','cop_incident_details.address','ref_user.first_name','ref_user.last_name', 'ref_incident_subcategory.sub_category_name',
            'cop_incident_details.incident_description', 'cop_incident_details.created_at')
		->join('cop_incident_details','cop_incident_details.id','=','cop_user_incidents_closed.cop_incident_details_id')
		->join('ref_incident_subcategory', 'ref_incident_subcategory.id', '=', 'cop_incident_details.ref_incident_subcategory_id')
		->join('ref_user','ref_user.id','=','cop_incident_details.created_by')->where('cop_incident_details.status',3);
		
		if($request->fromdate != "" && $request->todate != "" )
		{		    
		    $from = date_format( new DateTime($request->fromdate), 'Y-m-d');		 
		    $to = date_format( new DateTime($request->todate), 'Y-m-d');
		    
	        $users = $users->whereBetween(DB::Raw("DATE_FORMAT(cop_incident_details.created_at, '%Y-%m-%d')"),[$from,$to]);		        
		}
		
		$users = $users->get();
			
		foreach($users as $k=>$u)
		{
		    $users[$k]->date = Carbon::parse($u->created_at)->format('d/m/y H:i');		    
		}
		
		return Datatables::of($users)->addColumn('firstlast', function($row){
            return $row->first_name.' '.$row->last_name;
		})
		 ->addColumn('statuss',function($rows){ 
		 	//echo $rows->status; die;
		 	if ($rows->status == 1) { return Lang::get("pages.Onwait"); }
		 	else if($rows->status == 2){ return Lang::get("pages.Pending"); }
		 	else if($rows->status == 3){ return Lang::get("pages.Finished"); }
		 	else { return Lang::get("pages.Finished"); }
		 })
		 ->addColumn('view', function($userview){
				return '<a href="javascript:void(0)" id="viewIncident" rel="'.$userview->id.'" data-toggle="modal" data-target="#myModal"><i class="fa fa-angle-right" aria-hidden="true"></i></a>';
			})->rawColumns(array("view"))
			->filter(function ($instance) use ($request) {
			    if ($request->has('first_name') && $request->first_name != "") {
			        $instance->collection = $instance->collection->filter(function ($row) use ($request) {
			            
			            return Str::contains(strtolower($row['first_name']."".$row['last_name']), strtolower($request->get('first_name'))) ? true : false;
			        });
			    }			    
			})
			->make(true);
	}
	
	public function viewincident(Request $request)
    {
    	$incidentid = $request->incidentid;
    	if(empty($incidentid)) return response()->json(['status'=>false, 'message'=>Lang::get("pages.pleaseprovideincidentid")], 200);

    	try {
            $userData = \DB::table('cop_user_incidents_closed')->select('cop_user_incidents_closed.id','cop_user_incidents_closed.comment','cop_user_incidents_closed.signature','cop_user_incidents_closed.created_at as closedate','cop_user_incidents_closed.reference as closereference','cop_incident_details.status','cop_incident_details.reference','cop_incident_details.updated_on','cop_incident_details.address','ref_user.first_name','ref_user.last_name','rfc.first_name as cop_first_name','rfc.last_name as cop_last_name', 'ref_incident_subcategory.sub_category_name',
	            'cop_incident_details.incident_description','cop_incident_attachment.photo','cop_incident_attachment.video')
			->join('cop_incident_details','cop_incident_details.id','=','cop_user_incidents_closed.cop_incident_details_id')
			->join('ref_incident_subcategory', 'ref_incident_subcategory.id', '=', 'cop_incident_details.ref_incident_subcategory_id')
			->join('ref_user','ref_user.id','=','cop_incident_details.created_by')
			->join('ref_user as rfc','rfc.id','=','cop_user_incidents_closed.created_by')
			->leftJoin('cop_incident_attachment','cop_incident_details.id','=','cop_incident_attachment.cop_incident_details_id')
			->where('cop_user_incidents_closed.id', $incidentid)
			->get(); 
           
            if($userData->isEmpty()) return response()->json(['status'=>false, 'message'=>Lang::get("pages.somethingwentwrong")], 200);
            
            return response()->json(['status'=>true, 'data'=>$userData], 200);
        } catch (QueryException $e) {
            return response()->json(['status'=>false, 'message'=>$e->getMessage()], 200);
        }
    }
	
	public function currenthanddata(Request $request)
    {
			if($request->fromdate != "" && $request->todate != "" )
			{
			
			$from = date_format( new DateTime($request->fromdate), 'Y-m-d');
			//print_r($from); die;
// 			$to = date_format( new DateTime($request->todate), 'h:i:s');
			$to = date_format( new DateTime($request->todate), 'Y-m-d');
// 			$dat = $from.$to;
// 			$now = date('Y-m-d H:i:s');
			
			//\DB::enableQueryLog();
			$users = \DB::table('cop_handrail')->select('cop_handrail.id','cop_handrail.status','cop_handrail.address','cop_handrail.updated_on','ref_user.first_name','ref_user.last_name', 'cop_handrail.object',
				    'cop_handrail.signature', 'cop_handrail.created_at')
					->join('ref_user','ref_user.id','=','cop_handrail.created_by')
			->whereBetween(DB::Raw("DATE_FORMAT(cop_handrail.created_at, '%Y-%m-%d')"),[$from,$to])
			->orderBy('cop_handrail.created_at', 'desc')
			->get();
// 			$users = \DB::table('cop_handrail')->select('cop_handrail.id','cop_handrail.status','cop_handrail.address','cop_handrail.updated_on','ref_user.first_name','ref_user.last_name', 'cop_handrail.object',
// 			    'cop_handrail.signature', 'cop_handrail.created_at')
// 			->join('ref_user','ref_user.id','=','cop_handrail.created_by')
// 			->whereBetween(DB::Raw("DATE_FORMAT(cop_handrail.created_at, '%Y-%m-%d %H:%m:%s')"),[$dat,$now])
// 			->get();
			}
			else
			{
				
			/*$users = \DB::table('cop_user_incidents_closed')->select('cop_user_incidents_closed.id','cop_incident_details.status','cop_incident_details.address','cop_incident_details.updated_on','ref_user.first_name','ref_user.last_name', 'ref_incident_subcategory.sub_category_name',
	            'cop_incident_details.incident_description')
			->join('cop_incident_details','cop_incident_details.id','=','cop_user_incidents_closed.cop_incident_details_id')
			->join('ref_incident_subcategory', 'ref_incident_subcategory.id', '=', 'cop_incident_details.ref_incident_subcategory_id')
			->join('ref_user','ref_user.id','=','cop_incident_details.created_by')	
			->get();*/
			$users = \DB::table('cop_handrail')->select('cop_handrail.id','cop_handrail.status','cop_handrail.address','cop_handrail.updated_on','ref_user.first_name','ref_user.last_name', 'cop_handrail.object',
			    'cop_handrail.signature', 'cop_handrail.created_at')
			->join('ref_user','ref_user.id','=','cop_handrail.created_by')
			->orderBy('cop_handrail.created_at', 'desc')
			->get();
			}
			 //dd(\DB::getQueryLog());
			if(!$users->isEmpty())
			{
			    foreach ($users as $k=>$u)
			    {
			        $users[$k]->date = Carbon::parse($u->created_at)->format('d/m/y H:i');
			        // $users[$k]->date = Carbon::parse($u->created_at)->format('Y-m-d H:i');
			    }
			}
			 return Datatables::of($users) ->addColumn('firstlast', function($row){
	     			 return $row->first_name.' '.$row->last_name;
			})
			 ->addColumn('statuss',function($rows){ 
			 	//echo $rows->status; die;
			 	if ($rows->status == 1) { return Lang::get("pages.Onwait"); }
			 	else if($rows->status == 2){ return Lang::get("pages.Pending"); }
			 	else if($rows->status == 3){ return Lang::get("pages.Finished"); }
			 	else { return Lang::get("pages.Finished"); }
			 })
			 ->addColumn('view', function($userview){
					return '<a href="javascript:void(0)" id="viewhandrail" rel="'.$userview->id.'" data-toggle="modal" data-target="#myModal"><i class="fa fa-angle-right" aria-hidden="true"></i></a>';
				})->rawColumns(array("view"))
			 	->filter(function ($instance) use ($request) {
	                if ($request->has('first_name') && $request->first_name != "") {
	                    $instance->collection = $instance->collection->filter(function ($row) use ($request) {
	                 
	                        return Str::contains(strtolower($row['first_name']."".$row['last_name']), strtolower($request->get('first_name'))) ? true : false;
	                    });
	                }
	           		/*elseif ($request->has('first_name') && $request->first_name != "") {
	                    $instance->collection = $instance->collection->filter(function ($row) use ($request) {
	                 
	                        return Str::contains(strtolower($row['first_name']."".$row['last_name']), strtolower($request->get('first_name'))) ? true : false;
	                    });
	                }*/
	           
	               
	            })->make(true);
		
    }
	
	public function viewhandrail(Request $request)
    {
    	$incidentid = $request->incidentid;
    	if(empty($incidentid)) return response()->json(['status'=>false, 'message'=>Lang::get("pages.pleaseprovideincidentid")], 200);

    	try {
            /*$userData = \DB::table('cop_user_incidents_closed')->select('cop_user_incidents_closed.id','cop_user_incidents_closed.comment','cop_user_incidents_closed.created_at as closedate','cop_user_incidents_closed.reference as closereference','cop_incident_details.status','cop_incident_details.reference','cop_incident_details.updated_on','cop_incident_details.address','ref_user.first_name','ref_user.last_name','rfc.first_name as cop_first_name','rfc.last_name as cop_last_name', 'ref_incident_subcategory.sub_category_name','cop_incident_attachment.photo','cop_incident_attachment.video',
	            'cop_incident_details.incident_description')
			->join('cop_incident_details','cop_incident_details.id','=','cop_user_incidents_closed.cop_incident_details_id')
			->join('ref_incident_subcategory', 'ref_incident_subcategory.id', '=', 'cop_incident_details.ref_incident_subcategory_id')
			->join('ref_user','ref_user.id','=','cop_incident_details.created_by')
			->join('ref_user as rfc','rfc.id','=','cop_user_incidents_closed.created_by')
			->leftJoin('cop_incident_attachment','cop_incident_details.id','=','cop_incident_attachment.cop_incident_details_id')
			->where('cop_user_incidents_closed.id', $incidentid)
			->get(); */
           $userData = \DB::table('cop_handrail')->select('cop_handrail.id','cop_handrail.address','cop_handrail.object','cop_handrail.description','cop_handrail.signature','cop_handrail.reference','cop_handrail.updated_on','cop_handrail.address','ref_user.first_name','ref_user.last_name','cop_handrail_attachment.photo','cop_handrail_attachment.video'
	            )
           ->join('ref_user','ref_user.id','=','cop_handrail.created_by')
           ->leftJoin('cop_handrail_attachment','cop_handrail.id','=','cop_handrail_attachment.cop_handrail_id')
			->where('cop_handrail.id', $incidentid)
			->get();
            if($userData->isEmpty()) return response()->json(['status'=>false, 'message'=>Lang::get("pages.somethingwentwrong")], 200);
            
            return response()->json(['status'=>true, 'data'=>$userData], 200);
        } catch (QueryException $e) {
            return response()->json(['status'=>false, 'message'=>$e->getMessage()], 200);
        }
    }
	
    public function userdata(Request $request)
    {
        //\DB::enableQueryLog();
    	 $users = User::where([['ref_user_type_id','=',UserType::_TYPE_OPERATOR],['approved','=',1]]);
    	 if($request->registration_start_date !="" && $request->registration_end_date){
//     	     $users = $users->where(DB::Raw("DATE_FORMAT('created_at', '%Y-%m-%d')"), Carbon::parse($request->registration_date)->format('Y-m-d'));
    	     $sdate = Carbon::parse($request->registration_start_date)->format('Y-m-d');
    	     $edate = Carbon::parse($request->registration_end_date)->format('Y-m-d');
    	     
    	     $users = $users->whereBetween(DB::Raw("DATE_FORMAT(created_at, '%Y-%m-%d')"),[$sdate, $edate]);
    	 }
    	 $users = $users->get(); // Approved User

		 foreach($users as $k=>$u)
		{
		    $users[$k]->date = Carbon::parse($u->created_at)->format('d/m/y H:i a');		    
		}
    	
        // dd(DB::getQueryLog());
        return Datatables::of($users)->addColumn('action', function ($user) {
                return '<div class="radio"><label><input type="radio" name="optradio"></label></div>';
            })->removeColumn('user_password')
			->addColumn('view', function($userview){
				return '<a href="javascript:void(0)" id="viewCops" rel="'.$userview->id.'" data-toggle="modal" data-target="#myModal"><i class="fa fa-angle-right" aria-hidden="true"></i></a>';
			})
			->rawColumns(array("action", "view"))
			->filter(function ($instance) use ($request) {
                if ($request->has('first_name') && $request->first_name != "") {
                    $instance->collection = $instance->collection->filter(function ($row) use ($request) {
                 
                        return Str::contains(strtolower($row['first_name']."".$row['last_name']), strtolower($request->get('first_name'))) ? true : false;
                    });
                }
            })->make(true);
    }
	
	public function viewUser(Request $request)
	{
		$userid = $request->userid;
		
		if(empty($userid)) return response()->json(['status'=>false, 'message'=>Lang::get("pages.Pleaseprovideuserid")], 200);
		
        try {
            $userData = User::where(array('id' => $userid))->get();            
            if($userData->isEmpty()) return response()->json(['status'=>false, 'message'=>Lang::get("pages.somethingwentwrong")], 200);
            
            foreach ($userData as $k => $v) 
            {
                $userData[$k]->first_name = ucfirst($v->first_name);
                $userData[$k]->last_name = ucfirst($v->last_name);
                $userData[$k]->profile_image = !empty($v->profile_image) ? asset('uploads/profile/'.$v->profile_image) : '';
                
                $userData[$k]->business_card1 = !empty($v->business_card1) ? asset('uploads/bussiness_cards/'.$v->business_card1) : '';
                $userData[$k]->business_card2 = !empty($v->business_card2) ? asset('uploads/bussiness_cards/'.$v->business_card2) : '';
                $userData[$k]->id_card1 = !empty($v->id_card1) ? asset('uploads/id_cards/'.$v->id_card1) : '';
                $userData[$k]->id_card2 = !empty($v->id_card2) ? asset('uploads/id_cards/'.$v->id_card2) : '';
                $userData[$k]->comment = CopApprovalComments::select('comment')->where(['ref_user_id'=>$v->id])->first();
                
                if(!empty($userData[$k]->comment)) $userData[$k]->comment = $userData[$k]->comment['comment'];

                if($v->ref_user_type_id == UserType::_TYPE_CITIZEN)
                {
                    $reportPoliceCount = count(IncidentDetail::where(['created_by'=>$v->id, 'ref_incident_category_id'=>1])->get());
                    $reportFireCount = count(IncidentDetail::where(['created_by'=>$v->id, 'ref_incident_category_id'=>2])->get());;
                    $reportCityCount = count(IncidentDetail::where(['created_by'=>$v->id, 'ref_incident_category_id'=>3])->get());;
                    $handrailCount = count(Handrail::where(['created_by'=>$v->id])->get()); ;
                    
                    
                    $userData[$k]->report_police = $reportPoliceCount;
                    $userData[$k]->report_fire = $reportFireCount;
                    $userData[$k]->report_city = $reportCityCount;
                    $userData[$k]->report_handrail = $handrailCount;                    
                }
                elseif ($v->ref_user_type_id == UserType::_TYPE_OPERATOR)
                {
                    $assignedIncidents = count(CopUserIncidentMapping::where('ref_user_id', $v->id)->get());
                    $completedIncidents = count(CopUserIncidentClosed::where(['created_by'=>$v->id])->get()); 
                    
                    $userData[$k]->assigned_incidents = $assignedIncidents;
                    $userData[$k]->completed_incidents = $completedIncidents;
                }  

            }
                        
            return response()->json(['status'=>true, 'data'=>$userData], 200);
        } catch (QueryException $e) {
            return response()->json(['status'=>false, 'message'=>$e->getMessage()], 200);
        }
	}
	
    public function userdatacitizen(Request $request)
    {
        $users = User::where('ref_user_type_id','=',UserType::_TYPE_CITIZEN)->orderBy('created_at', 'desc');
        if($request->registration_start_date !="" && $request->registration_start_date !=""){
            $sdate = Carbon::parse($request->registration_start_date)->format('Y-m-d');
            $edate = Carbon::parse($request->registration_end_date)->format('Y-m-d');
            $users = $users->whereBetween(DB::Raw("DATE_FORMAT(created_at, '%Y-%m-%d')"),[$sdate, $edate]);
        }
        $users = $users->get();
		
		
        
        foreach ($users as $k => $v)
        {	
			$users[$k]->date = Carbon::parse($v->created_at)->format('d/m/y H:i a');
            $totalReports = IncidentDetail::where('created_by', $v->id)->get();
            $users[$k]->total_reports = count($totalReports);
        }
        
        
        return Datatables::of($users)->addColumn('action', function ($user) {
                return '<div class="radio"><label><input type="radio" name="optradio"></label></div>';
            })->removeColumn('user_password')
			->addColumn('view', function($userview){
				return '<a href="javascript:void(0)" id="viewCops" rel="'.$userview->id.'" data-toggle="modal" data-target="#myModal"><i class="fa fa-angle-right" aria-hidden="true"></i></a>';
			})
			->rawColumns(array("action", "view"))
			->filter(function ($instance) use ($request) {
                if ($request->has('first_name') && $request->first_name != "") {
                    $instance->collection = $instance->collection->filter(function ($row) use ($request) {
                 
                        return Str::contains(strtolower($row['first_name']."".$row['last_name']), strtolower($request->get('first_name'))) ? true : false;
                    });
                }
            })->make(true);
    }

    
    public function archivecenter()
	{
		return view('backend.archivecenter');
	}
	
	public function chat()
	{
		return view('backend.chat');
	}
	
	public function listofincidents(Request $request)
	{
	    try {
			
			\DB::enableQueryLog();
	        $incidents = DB::table('cop_incident_details')->select('ref_incident_subcategory.sub_category_name',
	            'ref_user.first_name', 'ref_user.last_name', 'cop_incident_details.incident_description',
	            'cop_incident_details.other_description', 'cop_incident_details.reference', 'cop_incident_details.created_by',
	            'cop_incident_details.qr_code', 'cop_incident_details.latitude', 'cop_incident_details.longitude',
	            'cop_incident_details.address', 'ref_user.ref_user_type_id', 'cop_incident_details.city', 'cop_incident_details.created_at', 'cop_incident_details.id','ref_user.ref_user_type_id')
	            ->join('ref_incident_subcategory', 'ref_incident_subcategory.id', '=', 'cop_incident_details.ref_incident_subcategory_id')
	            ->join('ref_user', 'ref_user.id', '=', 'cop_incident_details.created_by')
	            ->orderBy('cop_incident_details.updated_at', 'DESC')->get();
			//dd(\DB::getQueryLog());
			
				//$query = "select ref_incident_subcategory.sub_category_name, ref_user.first_name, ref_user.last_name, cop_incident_details.incident_description, cop_incident_details.other_description, cop_incident_details.reference, cop_incident_details.created_by, cop_incident_details.qr_code, cop_incident_details.latitude, cop_incident_details.longitude, cop_incident_details.address, ref_user.ref_user_type_id, cop_incident_details.city, DATE_FORMAT(cop_incident_details.created_at, '%d/%m/%y %H:%i') as created_at, cop_incident_details.id, ref_user.ref_user_type_id from cop_incident_details inner join ref_incident_subcategory on ref_incident_subcategory.id = cop_incident_details.ref_incident_subcategory_id inner join ref_user on ref_user.id = cop_incident_details.created_by order by cop_incident_details.updated_at desc";
				//$incidents = DB::select($query);

	            if($request->input('lat') && $request->input('lng'))
	            {
	                $lat = $request->input('lat');
	                $lng = $request->input('lng');
	              	$query = "SELECT ref_incident_subcategory.sub_category_name,
              	             ref_user.first_name,ref_user.last_name, cop_incident_details.incident_description,
             	             cop_incident_details.other_description, cop_incident_details.reference, cop_incident_details.created_by,
            	             cop_incident_details.qr_code, cop_incident_details.latitude, cop_incident_details.longitude,
            	             cop_incident_details.address, ref_user.ref_user_type_id, cop_incident_details.city, cop_incident_details.created_at, cop_incident_details.id, 
                             ( 6371 * acos( cos( radians({$lat}) ) * cos( radians( cop_incident_details.latitude ) ) * cos( radians( cop_incident_details.longitude ) - radians($lng) ) + sin( radians($lat) ) * sin( radians( cop_incident_details.latitude ) ) ) ) AS distance 
                             FROM cop_incident_details
                             JOIN ref_incident_subcategory ON ref_incident_subcategory.id = cop_incident_details.ref_incident_subcategory_id
                             JOIN ref_user ON ref_user.id = cop_incident_details.created_by
                             HAVING `distance` <= 5 ORDER BY distance ASC";

	                $incidents = \DB::select($query);

	            }
               
	            foreach ($incidents as $k => $v)
	            {

	                $users = User::where('id', $v->created_by)->get();
	                $reporter = UserType::where('id', $users[0]->ref_user_type_id)->get()[0]->user_type;
					
					/*
					$dateTime = new \DateTime(trim($v->created_at));
					$dateTime = $dateTime->format('Y-m-d H:i');
					*/

	                $incidents[$k]->reporter = Lang::get("pages.$reporter");
					$incidents[$k]->created_date = Carbon::parse($v->created_at)->format('d/m/Y H:i');
					// $incidents[$k]->created_date = $v->created_at;
	                $incidents[$k]->status = '<span class="text-danger">'.Lang::get("pages.Onwait").'</span>';
	                $incidents[$k]->state = Lang::get("pages.Onwait");
	                
	                # Validate if incident has relavent entry in mapping table
	                $incidentMapping = CopUserIncidentMapping::where('cop_incident_details_id', $v->id)->get();
 
	                if(!$incidentMapping->isEmpty())
	                {
	                    $incidents[$k]->status = '<span class="text-primary">'.Lang::get("pages.Pending").'</span>';
	                    $incidents[$k]->state = Lang::get("pages.Pending");
	                    # Incident has mapping available, Check if incident is closed ?
                        
	                    $incidentClosedMapping = CopUserIncidentClosed::where('cop_incident_details_id', $v->id)->get();
	                    if(!$incidentClosedMapping->isEmpty())  {
	                        $incidents[$k]->status = '<span class="text-success">'.Lang::get("pages.Finished").'</span>';
	                        $incidents[$k]->state = Lang::get("pages.Finished");
	                    }
	                }	                
	                
	                $incidents[$k]->status = $incidents[$k]->status;
	                $incidents[$k]->state = $incidents[$k]->state;
					
	            }
	            //dd($incidents);
	            return Datatables::of($incidents)->editColumn('status', function($i){
	                return $i->status.'<a href="javascript:void(0)" class="view-incident" data-state="'.$i->state.'" data-incident-id="'.$i->id.'" data-toggle="modal" data-target="#myModal"><i class="fa fa-angle-right" aria-hidden="true"></i></a>';
				})
	            ->rawColumns(['status'])->make(true);
	            
	    } catch (Exception $e) 
	    {
	       
	    }
	}
	
	public function incidentdetail(Request $request)
	{
	    $incidentId = $request->input('incident-id');
	    return response()->json(['data'=> IncidentDetail::where('id', $incidentId)->get()], 200);
	}
	
	public function updategrade(Request $request)
	{
	    if($request->ajax())
	    {
	        # Validate if user id exists
	        if(empty($request->input('user-id')))  return response()->json(['status'=>false, 'message'=>Lang::get("pages.Invalidrequest")], 200);
	        
	        $rs = User::where(['id'=>$request->input('user-id')])->update(['cops_grade'=>$request->input('grade')]);
	        if($rs) return response()->json(['status'=>true, 'message'=>'Grade updated successfully !'], 200);	        
	        return response()->json(['status'=>false], 200);
	    }
	    return response()->json(['status'=>false, 'message'=>Lang::get("pages.Invalidrequest")], 200);
	}
	
	public function updateaccount(Request $request)
	{
	    if($request->ajax())
	    {
	        # Validate if user id exists
	        if(empty($request->input('user-id')))  return response()->json(['status'=>false, 'message'=>Lang::get("pages.Invalidrequest")], 200);
	        
	        $rs = User::where(['id'=>$request->input('user-id')])->update(['status'=>$request->input('type')]);
	        $message = $request->input('type') == 0 ? Lang::get("pages.freezedsuccess") : Lang::get("pages.unfreezedsuccess");
	        if($rs) return response()->json(['status'=>true, 'message'=>$message], 200);
	        return response()->json(['status'=>false], 200);
	    }
	    return response()->json(['status'=>false, 'message'=>Lang::get("pages.Invalidrequest")], 200);
	}
	
	public function approveaccount(Request $request)
	{
	    if($request->ajax())
	    {
	        # Validate if user id exists
	        if(empty($request->input('user-id')))  return response()->json(['status'=>false, 'message'=>Lang::get("pages.Invalidrequest")], 200);
	        
	        $rs = User::where(['id'=>$request->input('user-id')])->update(['approved' => 1]);	        
	        if($rs) return response()->json(['status'=>true, 'message'=>Lang::get("pages.accountapproved")], 200);
	        return response()->json(['status'=>false], 200);
	    }
	    return response()->json(['status'=>false, 'message'=>Lang::get("pages.Invalidrequest")], 200);
	}
	
	public function refuseaccount(Request $request)
	{
	    $userId = Auth::user()->id;
	    
	    if($request->ajax())
	    {
	        # Validate if user id exists
	        if(empty($request->input('user-id')))  return response()->json(['status'=>false, 'message'=>Lang::get("pages.Invalidrequest")], 200);
	        
	        $rs = User::where(['id'=>$request->input('user-id')])->update(['approved' => 2]);
	        if($rs) 
	        {
	            # Account refused mark entry to cop approval comment table
	            CopApprovalComments::create([
	                'ref_user_id'=>$request->input('user-id'), 
	                'comment' => $request->input('refuse-text'), 
	                'created_by' => $userId
	            ]);
	            
	            return response()->json(['status'=>true, 'message'=>Lang::get("pages.accountreject")], 200);
	        }
	        return response()->json(['status'=>false], 200);
	    }
	    return response()->json(['status'=>false, 'message'=>Lang::get("pages.Invalidrequest")], 200);
	}
	
	public function assignintervention(Request $request)
	{
	    $userId = Auth::user()->id;
	    	    
	    if($request->ajax())
	    {
	        # Validate if user id exists	        
	        if(empty($request->input('objectId')) || empty($request->input('operators-id'))) return response()->json(['status'=>false, 'message'=>Lang::get("pages.Invalidrequest")], 200);
	        	
	        $rs = false;
	
	        foreach ($request->input('operators-id') as $o) 
	        {
	           $rs = CopUserIncidentTempMapping::create([
	               'ref_user_id' => $o, 
	               'cop_incident_details_id' => $request->input('objectId'), 
	               'ref_user_created_by' => $userId,
	               'ref_incident_status_id' => 1
	           ]);
			   			   
			   $incidentDetails= DB::table('cop_incident_details')->select('cop_incident_details.id', 'ref_incident_subcategory.sub_category_name','cop_incident_details.other_description','cop_incident_details.reference','cop_incident_details.address','cop_incident_details.created_at',
				'cop_incident_details.incident_description','cop_incident_details.status')
				->join('ref_incident_subcategory', 'ref_incident_subcategory.id', '=', 'cop_incident_details.ref_incident_subcategory_id')
				->where('cop_incident_details.id', $request->input('objectId'))
				->get();
			  			   
	           # Once intervention is assigned, send push notification
	           $push = new \Edujugon\PushNotification\PushNotification('fcm');
			   
			   $incidents = json_encode((array)$incidentDetails[0]);			   

				$userData = User::where('id', $o)->get();

				$lang = $userData->isEmpty() ? 'En' : $userData[0]->device_lang;
			   	$msg = ResponseMessage::statusResponses(ResponseMessage::_STATUS_NEW_INTERVENTION_ASSIGNED, strtolower($lang));

				$message = array(
					'message'=> $msg, 
					'incident'=>$incidents, 
					'type'=>'incident'
				);



	           $push->setMessage([
	            //    'notification' => [
	            //        'title'=>'A new intervention has been assigned to you',
	            //        'body'=>"{'message' : 'A new intervention has been assigned to you', 'incident': $incidents, 'type':'incident'}",
	            //        'sound' => 'default',	                   
				//    ],
				   'data' => $message
	           ]);
	           
			   
			   /*
	           $data = array(
					$incidentDetails->toArray()[0]
	           );
			   */

	           /*
	           if(!$incidentDetails->isEmpty())
	           {
	           		
	           		echo "<pre>";	           		
	           		array_merge($data, (array)$t[0]);
	           }
				*/

	          # print_r(json_encode($data)); die;
			   
			  /*
			  	$push->setMessage([
	           		'data'=>$incidentDetails[0]
	           	]);
				*/
				$tokenData = UserDeviceMapping::where('ref_user_id', $o)->get();	           					
				# Get device token of the user
			   if(!$tokenData->isEmpty())
			   {
					$push->setDevicesToken($tokenData[0]->device_token);
					$push->send();
					$push->getFeedback(); 
			   }	           
	        }
	        
	        if($rs)
	        {   
	               # New user registration notification
                Notification::create([
                    'table_id' =>$userId,
                    'table' => 'cop_user_incident_mapping', 
                    'message' => 'intervention assigned'                    
                ]);
	            #IncidentDetail::where('id', $request->input('objectId'))->update(['status'=>2]);
	            return response()->json(['status'=>true, 'message'=>Lang::get("pages.interventionassigned")], 200);
	        }
	        return response()->json(['status'=>false], 200);
	    }
	    return response()->json(['status'=>false, 'message'=>Lang::get("pages.Invalidrequest")], 200);
	}
	
    public function viewincidentdata(Request $request)
    {
    	$incidentid = $request->incidentid;
    	if(empty($incidentid)) return response()->json(['status'=>false, 'message'=>Lang::get("pages.pleaseprovideincidentid")], 200);

    	try {
    		$incidents = DB::table('cop_incident_details')->select('ref_incident_subcategory.sub_category_name',
	            'ref_user.first_name', 'ref_user.last_name','ref_user.date_of_birth','ref_user.email_id','ref_user.id as user_id','ref_user.ref_user_type_id', 'cop_incident_details.incident_description',
	            'cop_incident_details.other_description', 'cop_incident_details.reference', 'cop_incident_details.created_by',
	            'cop_incident_details.qr_code', 'cop_incident_details.latitude', 'cop_incident_details.longitude',
	            'cop_incident_details.address', 'cop_incident_details.city', 'cop_incident_details.created_at', 'cop_incident_details.id','cop_incident_attachment.photo','cop_incident_attachment.video')
	            ->join('ref_incident_subcategory', 'ref_incident_subcategory.id', '=', 'cop_incident_details.ref_incident_subcategory_id')
	            ->join('ref_user', 'ref_user.id', '=', 'cop_incident_details.created_by')
	            ->leftJoin('cop_incident_attachment','cop_incident_details.id','=','cop_incident_attachment.cop_incident_details_id')
				->where('cop_incident_details.id', $incidentid)
				->get(); 
			
            if($incidents->isEmpty()) return response()->json(['status'=>false, 'message'=>Lang::get("pages.somethingwentwrong")], 200);
            
            foreach ($incidents as $k => $v) 
            {
                $incidents[$k]->first_name = ucfirst($v->first_name);
                $incidents[$k]->last_name = ucfirst($v->last_name);
                $incidents[$k]->profile_image = !empty($v->profile_image) ? asset('uploads/profile/'.$v->profile_image) : '';
                
                $incidents[$k]->business_card1 = !empty($v->business_card1) ? asset('uploads/bussiness_cards/'.$v->business_card1) : '';
                $incidents[$k]->business_card2 = !empty($v->business_card2) ? asset('uploads/bussiness_cards/'.$v->business_card2) : '';
                $incidents[$k]->id_card1 = !empty($v->id_card1) ? asset('uploads/id_cards/'.$v->id_card1) : '';
                $incidents[$k]->id_card2 = !empty($v->id_card2) ? asset('uploads/id_cards/'.$v->id_card2) : '';
                
                if($v->ref_user_type_id == UserType::_TYPE_CITIZEN)
                {
                    $reportPoliceCount = count(IncidentDetail::where(['created_by'=>$v->user_id, 'ref_incident_category_id'=>1])->get());
                    $reportFireCount = count(IncidentDetail::where(['created_by'=>$v->user_id, 'ref_incident_category_id'=>2])->get());;
                    $reportCityCount = count(IncidentDetail::where(['created_by'=>$v->user_id, 'ref_incident_category_id'=>3])->get());;
                    $handrailCount = count(Handrail::where(['created_by'=>$v->user_id])->get()); ;
                    
                    
                    $incidents[$k]->report_police = $reportPoliceCount;
                    $incidents[$k]->report_fire = $reportFireCount;
                    $incidents[$k]->report_city = $reportCityCount;
                    $incidents[$k]->report_handrail = $handrailCount;                    
                }
                elseif ($v->ref_user_type_id == UserType::_TYPE_OPERATOR)
                {
                    $assignedIncidents = count(CopUserIncidentMapping::where('ref_user_id', $v->id)->get());
                    $completedIncidents = count(CopUserIncidentClosed::where(['created_by'=>$v->id])->get()); 
                    
                    $incidents[$k]->assigned_incidents = $assignedIncidents;
                    $incidents[$k]->completed_incidents = $completedIncidents;
                }       
            }
            
            return response()->json(['status'=>true, 'data'=>$incidents], 200);
        } catch (QueryException $e) {
            return response()->json(['status'=>false, 'message'=>$e->getMessage()], 200);
        }
    }
	
	public function listofincidentscityzencops(Request $request)
	{
	    //dd($request->input('usertype'));
	    try {
	    	if($request->input('usertype'))
	    	{
	    	$user_type_id = $request->input('usertype');
	        $incidents = DB::table('cop_incident_details')->select('ref_incident_subcategory.sub_category_name',
	            'ref_user.first_name', 'ref_user.last_name', 'cop_incident_details.incident_description',
	            'cop_incident_details.other_description', 'cop_incident_details.reference', 'cop_incident_details.created_by',
	            'cop_incident_details.qr_code', 'cop_incident_details.latitude', 'cop_incident_details.longitude',
	            'cop_incident_details.address', 'cop_incident_details.city', 'cop_incident_details.created_at', 'cop_incident_details.id','ref_user.ref_user_type_id')
	            ->join('ref_incident_subcategory', 'ref_incident_subcategory.id', '=', 'cop_incident_details.ref_incident_subcategory_id')
	            ->join('ref_user', 'ref_user.id', '=', 'cop_incident_details.created_by')
	            ->whereIn('ref_user.ref_user_type_id', $user_type_id)
	            ->orderBy('cop_incident_details.updated_at', 'DESC')->get();
	            
	            if($request->input('lat') && $request->input('lng'))
	            {
	                
	                $condition = ""; 
	                if(!empty($request->input('usertype'))){	                    
	                    $users = implode(',',$request->input('usertype'));	                    
	                    $condition = " where ref_user.ref_user_type_id in($users)";
	                }
	                \DB::enableQueryLog();
	                $lat = $request->input('lat');
	                $lng = $request->input('lng');
	                $query = "SELECT ref_incident_subcategory.sub_category_name,
              	             ref_user.first_name,ref_user.last_name, cop_incident_details.incident_description,
             	             cop_incident_details.other_description, cop_incident_details.reference, cop_incident_details.created_by,
            	             cop_incident_details.qr_code, cop_incident_details.latitude, cop_incident_details.longitude,
            	             cop_incident_details.address, cop_incident_details.city, cop_incident_details.created_at, cop_incident_details.id, ref_user.ref_user_type_id, 
                             ( 6371 * acos( cos( radians({$lat}) ) * cos( radians( cop_incident_details.latitude ) ) * cos( radians( cop_incident_details.longitude ) - radians($lng) ) + sin( radians($lat) ) * sin( radians( cop_incident_details.latitude ) ) ) ) AS distance 
                             FROM cop_incident_details
                             JOIN ref_incident_subcategory ON ref_incident_subcategory.id = cop_incident_details.ref_incident_subcategory_id
                             JOIN ref_user ON ref_user.id = cop_incident_details.created_by " .$condition. "
                             HAVING `distance` <= 5 ORDER BY distance ASC";
                    
	                $incidents = \DB::select($query);
//                     dd(\DB::getQueryLog());
	            }

	            foreach ($incidents as $k => $v)
	            {
	                $users = User::where('id', $v->created_by)->get();

	                $reporter = UserType::where('id', $users[0]->ref_user_type_id)->get()[0]->user_type;
	                
	                $incidents[$k]->reporter = $reporter;
	                $incidents[$k]->date = Carbon::parse($v->created_at)->format('Y-m-d');
	                $incidents[$k]->status = '<span class="text-danger">On-Wait</span>';
	                
	                # Validate if incident has relavent entry in mapping table
	                $incidentMapping = CopUserIncidentMapping::where('cop_incident_details_id', $v->id)->get();
	                if(!$incidentMapping->isEmpty())
	                {
	                    $incidents[$k]->status = '<span class="text-primary">Pending</span>';
	                    # Incident has mapping available, Check if incident is closed ?
                        
	                    $incidentClosedMapping = CopUserIncidentClosed::where('cop_incident_details_id', $v->id)->get();
                        if(!$incidentClosedMapping->isEmpty())  $incidents[$k]->status = '<span class="text-success">Finised</span>';
	                }	                
	                
	                $incidents[$k]->status = $incidents[$k]->status;
	            }
	            
	            return Datatables::of($incidents)->editColumn('status', function($incidents){
	                return $incidents->status.'<a href="javascript:void(0)" class="view-incident" data-incident-id="'.$incidents->id.'" data-toggle="modal" data-target="#myModal"><i class="fa fa-angle-right" aria-hidden="true"></i></a>';
	            })
	            ->rawColumns(['status'])->make(true);
	        }
	        
	        return response()->json(['flag'=>0]);
	            
	    } catch (Exception $e) 
	    {
	       
	    }
	}
	
	public function pushServerSentEvents()
	{    
		/*
	    $response =  new StreamedResponse();
	    $response->headers->set('Content-Type', 'text/event-stream');
	    $response->headers->set('Cache-Control', 'no-cache');
	    $response->setCallback(
	        function() {
	            $notifications = Notification::where('status', 0)->orderBy('created_at', 'desc')->get();
	            
	            if($notifications->isEmpty()) {
	                
	            }
	            
	            $res['count'] = count($notifications->toArray());
	            $res['data'] = $notifications->toArray();				
	            echo "data: ".json_encode($res)."\n\n";
	            ob_flush();
	            flush();
	            sleep(3);
	        });
		return $response->send();
		*/
		
		header('Content-Type: text/event-stream');
		header('Cache-Control: no-cache');
		
		do 
		{
			$notifications = Notification::where('status', 0)->orderBy('created_at', 'desc')->get();
			$count = count($notifications->toArray());		            

            echo "data: {\"notification\" : {$notifications}, \"count\" : {$count}}\n\n";
            sleep(2);
            @ob_flush();
            flush();

		}while(true);
	}
	
	public function updateNotificationStatus(Request $request)
	{
	    if($request->has('id') && $request->input('id') !="")
	    {
	        return Notification::where('id', $request->input('id'))->update(['status'=>1]);	        
	    }
	}

	public function userLiveLocation(Request $request)
	{
	    $userId = $request->input('user_id');
	    \DB::enableQueryLog();
	    $lastLocation = CopUserLocation::select(DB::Raw('distinct latitude'), 'longitude', DB::Raw('date(created_at) created_at'))->where(['user_id'=> $userId])->whereDate('created_at', '=', Carbon::now()->format('Y-m-d'))->orderBy('created_at', 'desc')->get();
	    // dd(\DB::getQueryLog());
	    $userData = User::where('id', $userId)->first();
// 	    dd($userData);
	    if($lastLocation->isEmpty()) return response()->json(['status'=>false]);
	    return response()->json(['status'=>true, 'data'=>$lastLocation->toArray(), 'ref_user_type_id'=>$userData['ref_user_type_id']]);
	}
	
	public function uploadMessageFile(Request $request)
	{
	    $lang = $request->input('lang');
	    
	    $rules = ['upload_document' => 'required|max:10000'];
	    $result = $this->validate_upload_request($request, $rules);
	    if($result) return $this->sendResponseMessage(array('status'=>false, 'message'=> $result), 200);
	    
	    $result = $this->uploadFile($request, 'uploads/messages', 'upload_document');
	    
	    if($result['status'] == true)
	    {
			$fileExtension = pathinfo($result['fileName'], PATHINFO_EXTENSION);
            
            if(in_array($fileExtension, $this->extensions))
            {
                $fileName = 'uploads/messages/thumb/'.$result['fileName'];
                Image::make(asset('uploads/messages').'/'.$result['fileName'])
                ->orientate()
                ->resize(null, 100, function($constraint){ 
                    $constraint->upsize();
                    $constraint->aspectRatio();
                })
                // ->resize(100, 100)
                ->save($fileName, 90);
                
                $result['thumb'] = asset('uploads/messages/thumb').'/'.$result['fileName'];
            }

            unset($result['message']);
            $result['message'] = $result['fileName'];
			$result['actual'] = asset('uploads/messages').'/'.$result['fileName'];
			
			/*
	        unset($result['message']);
			$result['message'] = asset('uploads/messages').'/'.$result['fileName'];
			*/
	    }
	    
	    return $this->sendResponseMessage($result, 200);
	}
	
	public function uploadFile($request, $destination=null, $key=null)
	{
	    if($request->hasFile($key))
	    {
	        $file = $request->file($key);
	        $size = $file->getSize();
	        
	        $extension = $file->getClientOriginalExtension();
	        $mime = $file->getMimeType();
	        
	        /*
	         * to be enabled in production
	         if($size > 5000000) return array('status'=>false, 'message'=>'File must not be greater than 5 MB');
	         if(!in_array($mime, $this->mimes)) return array('status'=>false, 'message'=>'Invalid Image Type');
	         if(!in_array($mime, $this->mimes)) return array('status'=>false, 'message'=>'Invalid Image Type');
	         if(!in_array($extension, $this->extensions)) return array('status'=>false, 'message'=>'Invalid Image Type');
	         */
	        
	        # Move Uploaded File
	        $destinationPath = 'uploads';
	        if($destination) $destinationPath = $destination;
	        
	        $fileName = uniqid().'.'.$extension;
	        if($file->move($destinationPath, $fileName)) return array('status'=>true, 'message'=> ResponseMessage::statusResponses(ResponseMessage::_STATUS_IMAGE_UPLOAD_SUCCESS), 'fileName'=>$fileName);
	        return array('status'=>false, 'message'=>'Something went wrong, please try again later');
	    }
	    
	    return array('status'=>false, 'message'=>ResponseMessage::statusResponses(ResponseMessage::_STATUS_NOTHING_TO_UPLOAD));
	}
	
	public function validate_upload_request($request, $rules)
	{
	    $validator  = Validator::make($request->all(), $rules);
	    if($validator->fails())
	    {
	        $messages = $validator->messages();
	        return $messages->first();
	    }
	}
	
	public function sendResponseMessage(array $message, $status=null)
	{
	    return response()->json($message, $status);
	}
}