<?php

namespace App\Http\Controllers\API;

use App\City;
use App\CopUserIncidentClosed;
use App\Handrail;
use App\HandrailAttachment;
use App\IncidentAttachment;
use App\IncidentCategory;
use App\IncidentDetail;
use App\IncidentSubcategory;
use App\User;
use App\UserDeviceMapping;
use App\Util\ResponseMessage;
use Carbon\Carbon;
use http\Env\Response;
use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\Validator;
use Mail;
use QrCode;
use Illuminate\Database\QueryException;
use App\CopUserHandrailMapping;
use App\CopUserIncidentMapping;

class ApiController extends Controller
{
    const _STAKEHOLDER_OPERATOR = 1;
    const _STAKEHOLDER_CITIZEN = 2;

    private static $OPENSSL_CIPHER_NAME = "aes-128-cbc"; //Name of OpenSSL Cipher
    private static $CIPHER_KEY_LEN = 16; //128

    private $iv = 'fedcba9876543210';
    private $key = '0123456789abcdef';

    private $mimes = array('image/jpeg','image/jpg', 'image/png');
    private $extensions = array("jpeg","jpg","png","JPEG","JPG","PNG");

    public function register(Request $request)
    {
        $payload = $this->get_payload($request);
        if(isset($payload['status']) && $payload['status'] === false)
        {
            return $this->sendResponseMessage(['status'=>false, 'message'=> ResponseMessage::statusResponses(ResponseMessage::_STATUS_DATA_NOT_FOUND)],200);
        }

        $rules  = [
            'gender'=>'required',
            'first_name'=>'required',
            'last_name'=>'required',
            'date_of_birth'=>'required',
            'phone_number'=>'required|unique:ref_user',
            'email_id'=>'required|unique:ref_user',
            'user_password'=>'required',
            'ref_user_type_id' =>'required'
        ];

        $result = $this->validate_request($payload, $rules);
        if($result) return $this->sendResponseMessage(array('status'=>false, 'message'=> $result), 200);

        /* Validation for keys in payload request */
//        $result = has_keys(array('gender', 'first_name', 'last_name', 'date_of_birth', 'phone_number',
//            'email_id', 'user_password', 'ref_user_type_id'), $payload);
//        if(!empty($result)) return $this->sendResponseMessage(array('status'=>false, 'message'=> ResponseMessage::statusResponses(ResponseMessage::_STATUS_DATA_NOT_FOUND)), 200);


        /* Validation passed now store user */

        $userId = uniqid();
        # Generate random otp
        $otp = rand(100000, 999999);
        $data = array(
            'gender'=> isset($payload['gender']) ? $payload['gender'] : '',
            'user_id' => $userId,
            'ref_user_type_id'=> $payload['ref_user_type_id'] == "Cops" ? static::_STAKEHOLDER_OPERATOR : static::_STAKEHOLDER_CITIZEN,
            'first_name'=> isset($payload['first_name']) ? $payload['first_name'] : '',
            'last_name'=> isset($payload['last_name']) ? $payload['last_name'] : '',
            'date_of_birth'=> isset($payload['date_of_birth']) ? $payload['date_of_birth'] : '',
            'place_of_birth'=> isset($payload['place_of_birth']) ? $payload['place_of_birth'] : '',
            'phone_number'=> isset($payload['phone_number']) ? $payload['phone_number'] : '',
            'email_id'=> isset($payload['email_id']) ? $payload['email_id'] : '',
            'user_password'=> $payload['user_password'],
            'approved'=> $payload['ref_user_type_id'] == "Cops" ? 0 : 1,
            'otp'=> $otp,
            'profile_qrcode'=>$userId.'.png'
        );

        if($payload['ref_user_type_id'] == "Cops")
        {
            $cardFront = $this->uploadFile($request, 'uploads/id_cards', 'id_card_front');
            if($cardFront['status'] == true)
            {
                $data['id_card1'] = $cardFront['fileName'];
            }

            $cardBack = $this->uploadFile($request, 'uploads/id_cards', 'id_card_back');
            if($cardBack['status'] == true)
            {
                $data['id_card2'] = $cardBack['fileName'];
            }

            $businessFront = $this->uploadFile($request, 'uploads/bussiness_cards', 'business_card_front');
            if($businessFront['status'] == true)
            {
                $data['business_card1'] = $businessFront['fileName'];
            }

            $businessBack = $this->uploadFile($request, 'uploads/bussiness_cards', 'business_card_back');
            if($businessBack['status'] == true)
            {
                $data['business_card2'] = $businessBack['fileName'];
            }
        }

        $profileImage = $this->uploadFile($request, 'uploads/profile', 'profile_image');

        if($profileImage['status'] == true)
        {
            $data['profile_image'] = $profileImage['fileName'];
        }

        try
        {
            $user = User::create($data);
            QrCode::format('png')->size(250)->generate($userId, public_path('uploads/profile/qrcodes/'.$userId.'.png'), 'image/png');
            if($user)
            {
                /*
                 * Add code here to map user id with device id
                 */
                $data = array(
                    'ref_user_id'=>$user->id,
                    'device_id'=>$payload['device_id'],
                    'created_on'=>Carbon::now()
                );
                try{
                    UserDeviceMapping::create($data);
                }catch (QueryException $e){
                    return $this->sendResponseMessage([
                        'status' => false,
                        'message'=> $e->getMessage()], 200);
                }

                $data = array(
                    'email' => $user->email_id,
                    'name' => $user->first_name.' '.$user->last_name,
                    'otp' => $otp
                );

                # Send OTP in email after successful registration
                Mail::send('mail.welcome', $data, function ($message) use ($data) {

                    $message->to($data['email']);
                    $message->subject('User Activation code');
                });

                return $this->sendResponseMessage([
                    'status' => true,
                    'id' => $user->id,
                    'userid' => $user->user_id,
                    'email_id' => $user->email_id,
                    'username' => $user->first_name.' '.$user->last_name,
                    'otp' => $otp,
                    'verified'=>0,
                    'message' => ResponseMessage::statusResponses(ResponseMessage::_STATUS_REGISTRATION_SUCCESS),
                    'profile_url' => empty($user->profile_image) ? '' : asset('uploads/profile').'/'.$user->profile_image,
                    'grade'=>$user->cops_grade,
                    'available'=>$user->available,
                    'level'=>1,
                    'profile_percent'=>0,
                    'total_reports'=>0,
                    'completed_reports'=>0,
                    'profile_qrcode'=>asset('uploads/profile/qrcodes').'/'.$user->profile_qrcode,
                ], 200);
            }
            else
            {
                return $this->sendResponseMessage([
                    'status' => false,
                    'message'=>ResponseMessage::statusResponses(ResponseMessage::_STATUS_REGISTRATION_FAILURE)
                ], 200);
            }
        }
        catch (QueryException $e)
        {
            return $this->sendResponseMessage([
                'status' => false,
                'message'=> $e->getMessage()], 200);
        }
    }


    public function validate_otp(Request $request)
    {
        $payload = $this->get_payload($request);
        if(isset($payload['status']) && $payload['status'] == false)
        {
            return $this->sendResponseMessage(['status'=>false, 'message'=> ResponseMessage::statusResponses(ResponseMessage::_STATUS_DATA_NOT_FOUND)],200);
        }

        $email = $payload['email_id'];
        $otp = $payload['otp'];

        #Validate user against email and otp
        $userData = User::where(['email_id'=>$email, 'otp'=>$otp])->get();

        if($userData->isEmpty()) return $this->sendResponseMessage(['status'=>false, 'message'=> ResponseMessage::statusResponses(ResponseMessage::_STATUS_DATA_NOT_FOUND)],200);

        # Update the status of citizen as verified
        $user = User::where(['email_id'=>$email, 'otp'=>$otp])->update(['verified'=>1]);

        if($user) return $this->sendResponseMessage(['status'=>true, 'message'=> ResponseMessage::statusResponses(ResponseMessage::_STATUS_OTP_VERIFIED_SUCCESS)],200);
        else return $this->sendResponseMessage(['status'=>false, 'message'=> ResponseMessage::statusResponses(ResponseMessage::_STATUS_OTP_VERIFIED_FAILURE)],200);
    }

    /**
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function login(Request $request)
    {
        $payload = $this->get_payload($request);
        if(isset($payload['status']) && $payload['status'] == false)
        {
            return $this->sendResponseMessage(['status'=>false, 'message'=> ResponseMessage::statusResponses(ResponseMessage::_STATUS_DATA_NOT_FOUND)],200);
        }

        /* Validation for keys in payload request */
//        $result = has_keys(array('email_id', 'user_password', 'ref_user_type_id'), $payload);
//        if(!empty($result)) return $this->sendResponseMessage(array('status'=>false, 'message'=> ResponseMessage::statusResponses(ResponseMessage::_STATUS_DATA_NOT_FOUND)), 200);

        $email = isset($payload['email_id']) ? $payload['email_id'] : '';
        $password = isset($payload['user_password']) ? $payload['user_password'] : '';
        $type = $payload['ref_user_type_id'] == "Cops" ? static::_STAKEHOLDER_OPERATOR : static::_STAKEHOLDER_CITIZEN;

        $rules  = [
            'email_id'=>'required',
            'user_password'=>'required',
        ];

        $result = $this->validate_request($payload, $rules);
        if($result) return $this->sendResponseMessage($result,200);

        $auth = User::where(array('email_id' => $email, 'user_password' => $password, 'ref_user_type_id'=>$type))->get();

        if($auth->isEmpty()) return $this->sendResponseMessage(array(
            'status'=>'false',
            'message' => ResponseMessage::statusResponses(ResponseMessage::_STATUS_INVALID_CREDENTIALS)), 200);

        return $this->sendResponseMessage(array(
            'status'=>'true',
            'id' => $auth[0]->id,
            'userid' => $auth[0]->user_id,
            'username' => $auth[0]->first_name.' '.$auth[0]->last_name,
            'email_id' => $auth[0]->email_id,
            'otp' => $auth[0]->otp,
            'profile_url' => empty($auth[0]->profile_image) ? '' : asset('uploads/profile').'/'.$auth[0]->profile_image,
            'verified' => $auth[0]->verified,
            'grade'=>$auth[0]->cops_grade,
            'available'=>$auth[0]->available,
            'level'=>1,
            'profile_percent'=>0,
            'total_reports'=>0,
            'completed_reports'=>0,
            'profile_qrcode'=>asset('uploads/profile/qrcodes').'/'.$auth[0]->profile_qrcode,
            'message' => ResponseMessage::statusResponses(ResponseMessage::_STATUS_LOGIN_SUCCESS)), 200);
    }

    public function reset_password(Request $request)
    {
        $payload = $this->get_payload($request);
        if(isset($payload['status']) && $payload['status'] == false)
        {
            return $this->sendResponseMessage(['status'=>false, 'message'=> ResponseMessage::statusResponses(ResponseMessage::_STATUS_DATA_NOT_FOUND)],200);
        }

        $email = $payload['email_id'];
        $type = $payload['ref_user_type_id'] == "Cops" ? static::_STAKEHOLDER_OPERATOR : static::_STAKEHOLDER_CITIZEN;

        # Check if data exists for user with provided email
        $user = User::where(['email_id'=>$email, 'ref_user_type_id'=>$type])->get();

        if($user->isEmpty()) return $this->sendResponseMessage(array('status'=>false, 'message'=> ResponseMessage::statusResponses(ResponseMessage::_STATUS_EMAIL_NOT_FOUND)), 200);

        # Generate new random password
        $password = generateRandomString(8);

        # Update password for user
        try{
            $rs = User::where(['email_id'=>$email])->update(['user_password'=>$password]);

            if($rs)
            {
                $data = array(
                    'email' => $email,
                    'password' => $password,
                    'name' => $user[0]->first_name.' '.$user[0]->last_name,
                );

                # Send password in email after successful registration
                Mail::send('mail.password_reset', $data, function ($message) use ($data) {

                    $message->to($data['email']);
                    $message->subject('User Password');
                });

                return $this->sendResponseMessage(array('status'=>'true', 'message' => ResponseMessage::statusResponses(ResponseMessage::_STATUS_PASSWORD_RESET_SUCCESS)), 200);
            }
            else {
                return $this->sendResponseMessage(array('status'=>'false', 'message' => ResponseMessage::statusResponses(ResponseMessage::_STATUS_PASSWORD_RESET_FAILURE)), 200);
            }
        }
        catch (QueryException $e){
            return $this->sendResponseMessage([
                'status' => false,
                'message'=> $e->getMessage()], 200);
        }
    }

    /*
     * Get list of incidents
     */
    public function get_incidents(Request $request)
    {
        $payload = $this->get_payload($request);
        if(isset($payload['status']) && $payload['status'] == false)
        {
            return $this->sendResponseMessage(['status'=>false, 'message'=> ResponseMessage::statusResponses(ResponseMessage::_STATUS_DATA_NOT_FOUND)],200);
        }

        $incidents = IncidentCategory::all();

        if($incidents->isEmpty()) return $this->sendResponseMessage(['flag'=>false, 'message'=> ResponseMessage::statusResponses(ResponseMessage::_STATUS_DATA_NOT_FOUND)],200);

        foreach ($incidents as $k=>$i)
        {
            $incidents[$k]->incident_id = $i->id;
            $incidents[$k]->incident_name = $i->category_name;
            $incidents[$k]->incident_description = $i->description;
            $incidents[$k]->incident_img_url = asset('/uploads/icon').'/'.$i->icon;

            unset($incidents[$k]->id);
            unset($incidents[$k]->category_name);
            unset($incidents[$k]->description);
            unset($incidents[$k]->icon);
            unset($incidents[$k]->is_deleted);
            unset($incidents[$k]->created_at);
            unset($incidents[$k]->updated_at);
            unset($incidents[$k]->updated_on);
        }

        return $this->sendResponseMessage(['flag'=>true, 'data'=> $incidents],200);
    }

    /*
     * Get list of sub incidenet by incident
     */
    public function get_incident_subcategory(Request $request)
    {
        $payload = $this->get_payload($request);
        if(isset($payload['status']) && $payload['status'] == false)
        {
            return $this->sendResponseMessage(['status'=>false, 'message'=> ResponseMessage::statusResponses(ResponseMessage::_STATUS_DATA_NOT_FOUND)],200);
        }

        $incidentId = $payload['incident_id'];
        $subIncidents = IncidentSubcategory::where(['ref_incident_category_id'=>$incidentId])->get();

        if($subIncidents->isEmpty()) return $this->sendResponseMessage(['status'=>false, 'message'=> ResponseMessage::statusResponses(ResponseMessage::_STATUS_DATA_NOT_FOUND)],200);

        foreach($subIncidents as $k=>$i)
        {
            $subIncidents[$k]->incident_id = $i->id;
            $subIncidents[$k]->incident_parent_id = $i->ref_incident_category_id;
            $subIncidents[$k]->incident_name = $i->sub_category_name;
            $subIncidents[$k]->incident_description = $i->description;
            $subIncidents[$k]->incident_img_url = asset('/uploads/icon').'/'.$i->icon;

            unset($subIncidents[$k]->id);
            unset($subIncidents[$k]->ref_incident_category_id);
            unset($subIncidents[$k]->sub_category_name);
            unset($subIncidents[$k]->description);
            unset($subIncidents[$k]->icon);
            unset($subIncidents[$k]->is_deleted);
            unset($subIncidents[$k]->created_at);
            unset($subIncidents[$k]->updated_at);
        }

        return $this->sendResponseMessage(['status'=>true, 'data'=> $subIncidents],200);
    }

    /*
     * Reports incident
     * params encrypted string with key data
     * return json string
     */
    public function report_incident(Request $request)
    {
        $payload = $this->get_payload($request);

        if(isset($payload['status']) && $payload['status'] == false)
        {
            return $this->sendResponseMessage(['status'=>false, 'message'=> ResponseMessage::statusResponses(ResponseMessage::_STATUS_DATA_NOT_FOUND)],200);
        }

        $rules =[
            'ref_incident_category_id'=>'required',
            'ref_incident_subcategory_id'=>'required',
            'incident_description'=>'required',
            'other_description'=>'required',
            'incident_lat'=>'required',
            'incident_lng'=>'required',
            'created_by'=>'required'
        ];
        $address_city = get_address_city($payload['incident_lat'], $payload['incident_lng']);
        if(isset($address_city['status']) && $address_city['status'] == false) return $this->sendResponseMessage(['status'=>false, 'message'=> ResponseMessage::statusResponses(ResponseMessage::_STATUS_DATA_NOT_FOUND)],200);

        $address = ""; $city = "";
        if(isset($address_city['results'][0]['formatted_address'])) $address = $address_city['results'][0]['formatted_address'];
        if(isset($address_city['results'][0]['address_components'][2]['long_name'])) $city = $address_city['results'][0]['address_components'][2]['long_name'];

        $result = $this->validate_request($payload, $rules);
        if($result) return $this->sendResponseMessage(array('status'=>false, 'message'=> $result), 200);

        /* Validation for handrail image and handrail video, at least 1 of the 2 should be sent in request */

//        if($request->hasFile('incident_image') || $request->hasFile('incident_video'))
//        {
            $referenceNo = 10000;
            try{
                $allIncidents = IncidentDetail::all();
            }catch (QueryException $e){
                return $this->sendResponseMessage(['status'=>false, 'message'=>  $e->getMessage()],200);
            }


            if(!$allIncidents->isEmpty()) $referenceNo = ($referenceNo+(count($allIncidents)+1));
            QrCode::format('png')->size(250)->generate($referenceNo, public_path('uploads/qrcodes/'.$referenceNo.'.png'), 'image/png');

            $data = array(
                'ref_incident_category_id'=>$payload['ref_incident_category_id'],
                'ref_incident_subcategory_id'=>$payload['ref_incident_subcategory_id'],
                'incident_description'=>$payload['incident_description'],
                'other_description'=>$payload['other_description'],
                'reference'=>$referenceNo,
                'qr_code'=>$referenceNo.'.png',
                'updated_on'=>Carbon::now(),
                'latitude' => $payload['incident_lat'],
                'longitude' => $payload['incident_lng'],
                'address' => $address,
                'city' => $city,
                'created_by'=>$payload['created_by']
            );

            $helplineNumber = IncidentCategory::where(['id'=>$payload['ref_incident_category_id']])->get()[0]->help_line_no;
            $attachment = array();
            $cardFront = $this->uploadFile($request, 'uploads/incident_image', 'incident_image');
            if ($cardFront['status'] == true) {
                $attachment['photo'] = $cardFront['fileName'];
            }

            $cardFront = $this->uploadFile($request, 'uploads/incident_video', 'incident_video');
            if ($cardFront['status'] == true) {
                $attachment['video'] = $cardFront['fileName'];
            }

            try{
                $rs = IncidentDetail::create($data);

                if(!empty($attachment))
                {
                    $attachment['cop_incident_details_id'] = $rs->id;
                    try{
                        IncidentAttachment::create($attachment);
                    }catch (QueryException $e){
                        return $this->sendResponseMessage(['status'=>false, 'message'=>  $e->getMessage()],200);
                    }
                }

                if($rs) return $this->sendResponseMessage(['status'=>true, 'message'=>  ResponseMessage::statusResponses(ResponseMessage::_STATUS_INCIDENT_ADD_SUCCESS), 'reference'=>$rs->reference, 'qrcode_url'=>asset('uploads/qrcodes').'/'.$referenceNo.'.png', 'helpline_number'=>$helplineNumber],200);
                return $this->sendResponseMessage(['status'=>false, 'message'=>  ResponseMessage::statusResponses(ResponseMessage::_STATUS_INCIDENT_ADD_FAILURE)],200);
            }
            catch(QueryException $e)
            {
                return $this->sendResponseMessage(['status'=>false, 'message'=>  $e->getMessage()],200);
            }
//        }

        return $this->sendResponseMessage(['status'=>false, 'message'=>  ResponseMessage::statusResponses(ResponseMessage::_STATUS_IMAGE_VIDEO_REQUIRED)],200);

    }

    /*
     * Reports handrail
     * params encrypted string with key data
     * return json string
     */
    public function report_handrail(Request $request)
    {
        $payload = $this->get_payload($request);

        if(isset($payload['status']) && $payload['status'] == false)
        {
            return $this->sendResponseMessage(['status'=>false, 'message'=> ResponseMessage::statusResponses(ResponseMessage::_STATUS_DATA_NOT_FOUND)],200);
        }

        $rules =[
            'objects'=>'required',
            'description'=>'required',
            'created_by'=>'required',
//            'handrail_lat'=>'required',
//            'handrail_lng'=>'required',
        ];

        $result = $this->validate_request($payload, $rules);
        if($result) return $this->sendResponseMessage(array('status'=>false, 'message'=> $result), 200);

        /* Validation for signature */
        if(!$request->hasFile('signature')) return $this->sendResponseMessage(array('status'=>false, 'message'=> ResponseMessage::statusResponses(ResponseMessage::_STATUS_SIGNATURE_REQUIRED)), 200);

        $sign = "";
        $signature = $this->uploadFile($request, 'uploads/signature', 'signature');
        if ($signature['status'] == true) {
            $sign = $signature['fileName'];
        }

        /* Validation for handrail image and handrail video, at least 1 of the 2 should be sent in request */

//        if($request->hasFile('handrail_image') || $request->hasFile('handrail_video'))
//        {
            $referenceNo = 100000;
            try{
                $allHandrails = Handrail::all();
            }catch (QueryException $e){
                return $this->sendResponseMessage(['status'=>false, 'message'=>  $e->getMessage()],200);
            }


            if(!$allHandrails->isEmpty()) $referenceNo = ($referenceNo+(count($allHandrails)+1));
            QrCode::format('png')->size(250)->generate($referenceNo, public_path('uploads/qrcodes/'.$referenceNo.'.png'), 'image/png');

            $data = array(
                'object'=>$payload['objects'],
                'description'=>$payload['description'],
                'reference'=>$referenceNo,
                'signature'=>$sign,
                'qr_code'=>$referenceNo.'.png',
                'updated_on'=>Carbon::now(),
                'created_by'=>$payload['created_by']
            );

            try{
                $rs = Handrail::create($data);

                $attachment = array();
                $cardFront = $this->uploadFile($request, 'uploads/handrail_image', 'handrail_image');
                if ($cardFront['status'] == true) {
                    $attachment['photo'] = $cardFront['fileName'];
                }

                $cardFront = $this->uploadFile($request, 'uploads/handrail_video', 'handrail_video');
                if ($cardFront['status'] == true) {
                    $attachment['video'] = $cardFront['fileName'];
                }

                if(!empty($attachment))
                {
                    $attachment['cop_handrail_id'] = $rs->id;
                    try{
                        HandrailAttachment::create($attachment);
                    }catch (QueryException $e){
                        return $this->sendResponseMessage(['status'=>false, 'message'=>  $e->getMessage()],200);
                    }

                }

                if($rs) return $this->sendResponseMessage(['status'=>true, 'message'=>  ResponseMessage::statusResponses(ResponseMessage::_STATUS_HANDRAIL_ADD_SUCCESS), 'reference'=>$referenceNo, 'qrcode_url'=>asset('uploads/qrcodes/').'/'.$referenceNo.'.png'],200);
                return $this->sendResponseMessage(['status'=>false, 'message'=>  ResponseMessage::statusResponses(ResponseMessage::_STATUS_HANDRAIL_ADD_FAILURE)],200);
            }
            catch (QueryException $e){
                return $this->sendResponseMessage(['status'=>false, 'message'=>  $e->getMessage()],200);
            }
//        }

        return $this->sendResponseMessage(['status'=>false, 'message'=>  ResponseMessage::statusResponses(ResponseMessage::_STATUS_IMAGE_VIDEO_REQUIRED)],200);
    }


    /**
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function get_incident_list(Request $request)
    {
        $payload = $this->get_payload($request);
		
        if(isset($payload['status']) && $payload['status'] == false)
        {
            return $this->sendResponseMessage(['status'=>false, 'message'=> ResponseMessage::statusResponses(ResponseMessage::_STATUS_DATA_NOT_FOUND)],200);
        }

        $rules =[
            'incident_lat'=>'required',
            'incident_lng'=>'required',
        ];

        $result = $this->validate_request($payload, $rules);
        if($result) return $this->sendResponseMessage(array('status'=>false, 'message'=> $result), 200);
        $lat = $payload['incident_lat'];
        $lng = $payload['incident_lng'];

        $query = "SELECT ref_incident_subcategory.sub_category_name, cop_incident_details.id, cop_incident_details.status, 
                  cop_incident_details.latitude, cop_incident_details.longitude, cop_incident_details.created_at, ( 6371 * acos( cos( radians({$lat}) ) * cos( radians( latitude ) ) * cos( radians( longitude ) - radians($lng) ) + sin( radians($lat) ) * sin( radians( latitude ) ) ) ) AS `distance`,
                  cop_incident_details.city, cop_incident_details.address  
                  FROM cop_incident_details 
                  JOIN ref_incident_subcategory ON ref_incident_subcategory.id = cop_incident_details.ref_incident_subcategory_id
                  HAVING `distance` <= 5 ORDER BY distance ASC";
        $rs = \DB::select($query);

        # Get all non deleted cities
        $cities = City::select('id', 'city_name')->where('is_deleted', 1)->get();

        if(empty($rs)) return $this->sendResponseMessage(['status'=>false, 'message'=> ResponseMessage::statusResponses(ResponseMessage::_STATUS_DATA_NOT_FOUND)],200);
        return $this->sendResponseMessage(['status'=>true, 'data'=> $rs, 'cities'=>$cities],200);
    }


    public function get_incident_by_city(Request $request)
    {
        $payload = $this->get_payload($request);
	

        if(isset($payload['status']) && $payload['status'] == false)
        {
            return $this->sendResponseMessage(['status'=>false, 'message'=> ResponseMessage::statusResponses(ResponseMessage::_STATUS_DATA_NOT_FOUND)],200);
        }

        $rules =[
            'city_id'=>'required'
        ];
        $result = $this->validate_request($payload, $rules);
        if($result) return $this->sendResponseMessage(array('status'=>false, 'message'=> $result), 200);

        $cityId = $payload['city_id'];
        # Get city name based on city id
        try{
            $cityData = City::findorfail($cityId);
            $city = $cityData['city_name'];

            $query = "SELECT ref_incident_subcategory.sub_category_name, cop_incident_details.id, cop_incident_details.status, 
                  cop_incident_details.latitude, cop_incident_details.longitude, cop_incident_details.created_at,
                  cop_incident_details.city, cop_incident_details.address
                  FROM cop_incident_details 
                  JOIN ref_incident_subcategory ON ref_incident_subcategory.id = cop_incident_details.ref_incident_subcategory_id
                  WHERE cop_incident_details.city LIKE '%$city%'";

            $rs = \DB::select($query);

            if(empty($rs)) return $this->sendResponseMessage(['status'=>false, 'message'=> ResponseMessage::statusResponses(ResponseMessage::_STATUS_DATA_NOT_FOUND)],200);
            return $this->sendResponseMessage(['status'=>true, 'data'=> $rs],200);

        }catch (QueryException $e){
            return $this->sendResponseMessage([
                'status' => false,
                'message'=> $e->getMessage()], 200);
        }
    }


    /**
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function get_cops_incident_list(Request $request)
    {
        $payload = $this->get_payload($request);

        if(isset($payload['status']) && $payload['status'] == false)
        {
            return $this->sendResponseMessage(['status'=>false, 'message'=> ResponseMessage::statusResponses(ResponseMessage::_STATUS_DATA_NOT_FOUND)],200);
        }

        $copId = $payload['user_id'];

        $res = \DB::table('cop_user_incident_mapping')
            ->select('ref_incident_subcategory.sub_category_name', 'cop_incident_details.id', 'cop_user_incident_mapping.status',
                  'cop_incident_details.latitude', 'cop_incident_details.longitude', 'cop_incident_details.created_at',
                  'cop_incident_details.city', 'cop_incident_details.address', 'cop_incident_details.incident_description',
                  'cop_incident_details.other_description', 'cop_incident_details.reference')
            ->join('cop_incident_details', 'cop_incident_details.id', '=', 'cop_user_incident_mapping.cop_incident_details_id')
            ->join('ref_incident_subcategory', 'ref_incident_subcategory.id', '=', 'cop_incident_details.ref_incident_subcategory_id')
            ->where(['ref_user_id'=>$copId])->get();

        if($res->isEmpty()) return $this->sendResponseMessage(['status'=>false, 'message'=> ResponseMessage::statusResponses(ResponseMessage::_STATUS_DATA_NOT_FOUND)],200);

        foreach ($res as $k => $v)
        {
            # Check if incident is already closed
            $status = $v->status;
            $closedIncident = CopUserIncidentClosed::where('cop_incident_details_id', $v->id)->get();

            $res[$k]->status =  $closedIncident->isEmpty() ? $status :  $closedIncident[0]['ref_incident_status_id'];
        }

        return $this->sendResponseMessage(['status'=>true, 'data'=> $res],200);
    }


    public function get_profile_attributes(Request $request)
    {
        $payload = $this->get_payload($request);

        if(isset($payload['status']) && $payload['status'] == false)
        {
            return $this->sendResponseMessage(['status'=>false, 'message'=> ResponseMessage::statusResponses(ResponseMessage::_STATUS_DATA_NOT_FOUND)],200);
        }

        $userId = $payload['user_id'];
        try{
            $user = User::where('id', $userId)->get();

            if($user->isEmpty()) return $this->sendResponseMessage(['status'=>false, 'message'=> ResponseMessage::statusResponses(ResponseMessage::_STATUS_DATA_NOT_FOUND)],200);
            return $this->sendResponseMessage([
                'status'=>true,
                'grade'=>$user[0]->cops_grade,
                'available'=>$user[0]->available,
                'level'=>1,
                'profile_percent'=>0,
                'total_reports'=>0,
                'completed_reports'=>0,
                'new_reports'=>0
            ],200);

        }catch (QueryException $e){
            return $this->sendResponseMessage([
                'status' => false,
                'message'=> $e->getMessage()], 200);
        }
    }

    public function set_available_status(Request $request)
    {
        $payload = $this->get_payload($request);

        if(isset($payload['status']) && $payload['status'] == false)
        {
            return $this->sendResponseMessage(['status'=>false, 'message'=> ResponseMessage::statusResponses(ResponseMessage::_STATUS_DATA_NOT_FOUND)],200);
        }

        $userId = $payload['user_id'];
        $available = $payload['available'];

        try{
            $rs = User::where('id', $userId)->update(['available'=>$available]);
            $responseKey = $available == 1 ? ResponseMessage::_STATUS_AVAILABILITY_SET_AVAILABLE : ResponseMessage::_STATUS_AVAILABILITY_SET_UNAVAILABLE;
            if($rs) return $this->sendResponseMessage(['status'=>true, 'available'=>$available, 'message'=> ResponseMessage::statusResponses($responseKey)],200);
            return $this->sendResponseMessage(['status'=>false, 'message'=> ResponseMessage::statusResponses(ResponseMessage::_STATUS_INVALID_OPERATION)],200);

        }catch (QueryException $e){
            return $this->sendResponseMessage([
                'status' => false,
                'message'=> $e->getMessage()], 200);
        }
    }

    public function close_registered_incident(Request $request)
    {
        $payload = $this->get_payload($request);

        if(isset($payload['status']) && $payload['status'] == false)
        {
            return $this->sendResponseMessage(['status'=>false, 'message'=> ResponseMessage::statusResponses(ResponseMessage::_STATUS_DATA_NOT_FOUND)],200);
        }

        $userId = $payload['user_id'];
        $incidentId = $payload['incident_id'];
        $comment = $payload['comment'];

        $data = array(
            'cop_incident_details_id' => $incidentId,
            'comment'=> $comment,
            'created_by'=> $userId,
            'ref_incident_status_id'=> 3,
        );
        try{
            $rs = CopUserIncidentClosed::create($data);

            /* let's update the status of incident in incident details table also */
            IncidentDetail::where('id', $incidentId)->update(['status'=>3]);

            if($rs) return $this->sendResponseMessage(['status'=>true, 'message'=> ResponseMessage::statusResponses(ResponseMessage::_STATUS_REGISTERED_INCIDENT_CLOSED_SUCCESS)],200);
            return $this->sendResponseMessage(['status'=>false, 'message'=> ResponseMessage::statusResponses(ResponseMessage::_STATUS_REGISTERED_INCIDENT_CLOSED_FAILURE)],200);
        }catch (QueryException $e){

        }

    }










    /* Util function for payload processing
     * @accepts encoded json string in request
     * @returns array
     */
    public function get_payload($request)
    {
        $payload = empty($request->all()) ?  file_get_contents('php://input') : $request->all();
        file_put_contents('uploads/test.txt',$payload);

        try
        {
            if(!isset($payload['data'])) return array('status'=>false, 'message'=> ResponseMessage::statusResponses(ResponseMessage::_STATUS_DATA_NOT_FOUND));
            $payload = json_decode($this->decrypt($this->key, $payload['data']), true);
            if($payload === null) return array('status'=> false);
            return $payload;
        }
        catch (\Exception $e)
        {
            return array('status'=>false, 'message'=> $e->getMessage());
        }
    }


    public function validate_request($payload, $rules)
    {
        $validator  = Validator::make($payload, $rules);
        if($validator->fails())
        {
            $messages = $validator->messages();
            return $messages->first();
        }
    }

    /*
     * Upload Image Helper
     */

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
    }

    /* API Return Response Messages */

    public function sendResponseMessage(array $message, $status=null)
    {
        return response()->json($message, $status);
    }

    /* Data encryption and decryption methods */

    /**
     * Encrypt data using AES Cipher (CBC) with 128 bit key
     *
     * @param type $key - key to use should be 16 bytes long (128 bits)
     * @param type $iv - initialization vector
     * @param type $data - data to encrypt
     * @return encrypted data in base64 encoding with iv attached at end after a :
     */

    public function encrypt($key, $iv, $data)
    {
        if (strlen($key) < static::$CIPHER_KEY_LEN) {
            $key = str_pad("$key", static::$CIPHER_KEY_LEN, "0"); //0 pad to len 16
        } else if (strlen($key) > static::$CIPHER_KEY_LEN) {
            $key = substr($str, 0, static::$CIPHER_KEY_LEN); //truncate to 16 bytes
        }

        $encodedEncryptedData = base64_encode(openssl_encrypt($data, static::$OPENSSL_CIPHER_NAME, $key, OPENSSL_RAW_DATA, $iv));
        $encodedIV = base64_encode($iv);
        $encryptedPayload = $encodedEncryptedData.":".$encodedIV;

        return $encryptedPayload;
    }

    /**
     * Decrypt data using AES Cipher (CBC) with 128 bit key
     *
     * @param type $key - key to use should be 16 bytes long (128 bits)
     * @param type $data - data to be decrypted in base64 encoding with iv attached at the end after a :
     * @return decrypted data
     */
    public function decrypt($key, $data) {
        if (strlen($key) < static::$CIPHER_KEY_LEN) {
            $key = str_pad("$key", static::$CIPHER_KEY_LEN, "0"); //0 pad to len 16
        } else if (strlen($key) > static::$CIPHER_KEY_LEN) {
            $key = substr($str, 0, static::$CIPHER_KEY_LEN); //truncate to 16 bytes
        }

        $parts = explode(':', $data); //Separate Encrypted data from iv.
        $decryptedData = openssl_decrypt(base64_decode($parts[0]), static::$OPENSSL_CIPHER_NAME, $key, OPENSSL_RAW_DATA, base64_decode($parts[1]));

        return $decryptedData;
    }
}
