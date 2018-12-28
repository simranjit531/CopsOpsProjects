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
use App\UserType;
use App\CopUserIncidentMapping;
use App\CopUserIncidentClosed;
use Illuminate\Database\QueryException;
use App\Handrail;
use App\CopApprovalComments;
use App\CopUserIncidentTempMapping;
use DateTime;

class BackendController extends Controller
{
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
        $operators = User::where(['ref_user_type_id' => UserType::_TYPE_OPERATOR, 'approved'=>1])->get();

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
    	return view('backend.dailycrew');
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
            'cop_incident_details.incident_description')
		->join('cop_incident_details','cop_incident_details.id','=','cop_user_incidents_closed.cop_incident_details_id')
		->join('ref_incident_subcategory', 'ref_incident_subcategory.id', '=', 'cop_incident_details.ref_incident_subcategory_id')
		->join('ref_user','ref_user.id','=','cop_incident_details.created_by')
		->get();
		 return Datatables::of($users) ->addColumn('firstlast', function($row){
      return $row->first_name.' '.$row->last_name;
		})
		 ->addColumn('statuss',function($rows){ 
		 	//echo $rows->status; die;
		 	if ($rows->status == 0) { return 'On-wait'; }
		 	else if($rows->status == 1){ return 'Pending'; }
		 	else if($rows->status == 2){ return 'Finished'; }
		 	else { return 'Finished'; }
		 })
		 ->addColumn('view', function($userview){
				return '<a href="javascript:void(0)" id="viewIncident" rel="'.$userview->id.'" data-toggle="modal" data-target="#myModal"><i class="fa fa-angle-right" aria-hidden="true"></i></a>';
			})->rawColumns(array("view"))->make(true);
	}
	
	public function viewincident(Request $request)
    {
    	$incidentid = $request->incidentid;
    	if(empty($incidentid)) return response()->json(['status'=>false, 'message'=>'Please provide Incident id'], 200);

    	try {
            $userData = \DB::table('cop_user_incidents_closed')->select('cop_user_incidents_closed.id','cop_user_incidents_closed.comment','cop_user_incidents_closed.created_at as closedate','cop_user_incidents_closed.reference as closereference','cop_incident_details.status','cop_incident_details.reference','cop_incident_details.updated_on','cop_incident_details.address','ref_user.first_name','ref_user.last_name','rfc.first_name as cop_first_name','rfc.last_name as cop_last_name', 'ref_incident_subcategory.sub_category_name',
	            'cop_incident_details.incident_description','cop_incident_attachment.photo','cop_incident_attachment.video')
			->join('cop_incident_details','cop_incident_details.id','=','cop_user_incidents_closed.cop_incident_details_id')
			->join('ref_incident_subcategory', 'ref_incident_subcategory.id', '=', 'cop_incident_details.ref_incident_subcategory_id')
			->join('ref_user','ref_user.id','=','cop_incident_details.created_by')
			->join('ref_user as rfc','rfc.id','=','cop_user_incidents_closed.created_by')
			->leftJoin('cop_incident_attachment','cop_incident_details.id','=','cop_incident_attachment.cop_incident_details_id')
			->where('cop_user_incidents_closed.id', $incidentid)
			->get(); 
           
            if($userData->isEmpty()) return response()->json(['status'=>false, 'message'=>'Something went wrong please try again later'], 200);
            
            return response()->json(['status'=>true, 'data'=>$userData], 200);
        } catch (QueryException $e) {
            return response()->json(['status'=>false, 'message'=>$e->getMessage()], 200);
        }
    }
	
	public function currenthanddata(Request $request)
    {
			if($request->fromdate != "" || $request->todate != "" )
			{
			$from = date_format( new DateTime($request->fromdate), 'Y-m-d');
			//print_r($from); die;
			$to = date_format( new DateTime($request->todate), 'Y-m-d');
			//\DB::enableQueryLog();
		    $users = \DB::table('cop_user_incidents_closed')->select('cop_user_incidents_closed.id','cop_incident_details.status','cop_incident_details.address','cop_incident_details.updated_on','ref_user.first_name','ref_user.last_name', 'ref_incident_subcategory.sub_category_name',
	            'cop_incident_details.incident_description')
			->join('cop_incident_details','cop_incident_details.id','=','cop_user_incidents_closed.cop_incident_details_id')
			->join('ref_incident_subcategory', 'ref_incident_subcategory.id', '=', 'cop_incident_details.ref_incident_subcategory_id')
			->join('ref_user','ref_user.id','=','cop_incident_details.created_by')
			->whereBetween(DB::Raw("DATE_FORMAT(cop_user_incidents_closed.created_at, '%Y-%m-%d')"),[$from,$to])
			->get();
			}
			else
			{
			$users = \DB::table('cop_user_incidents_closed')->select('cop_user_incidents_closed.id','cop_incident_details.status','cop_incident_details.address','cop_incident_details.updated_on','ref_user.first_name','ref_user.last_name', 'ref_incident_subcategory.sub_category_name',
	            'cop_incident_details.incident_description')
			->join('cop_incident_details','cop_incident_details.id','=','cop_user_incidents_closed.cop_incident_details_id')
			->join('ref_incident_subcategory', 'ref_incident_subcategory.id', '=', 'cop_incident_details.ref_incident_subcategory_id')
			->join('ref_user','ref_user.id','=','cop_incident_details.created_by')	
			->get();
			}
			// dd(\DB::getQueryLog());
			
			 return Datatables::of($users) ->addColumn('firstlast', function($row){
	     			 return $row->first_name.' '.$row->last_name;
			})
			 ->addColumn('statuss',function($rows){ 
			 	//echo $rows->status; die;
			 	if ($rows->status == 0) { return 'On-wait'; }
			 	else if($rows->status == 1){ return 'Pending'; }
			 	else if($rows->status == 2){ return 'Finished'; }
			 	else { return 'Finished'; }
			 })
			 ->addColumn('view', function($userview){
					return '<a href="javascript:void(0)" id="viewhandrail" rel="'.$userview->id.'" data-toggle="modal" data-target="#myModal"><i class="fa fa-angle-right" aria-hidden="true"></i></a>';
				})->rawColumns(array("view"))
			 	->filter(function ($instance) use ($request) {
	                if ($request->has('first_name') && $request->first_name != "") {
	                    $instance->collection = $instance->collection->filter(function ($row) use ($request) {
	                 
	                        return Str::contains(strtolower($row['first_name']), strtolower($request->get('first_name'))) ? true : false;
	                    });
	                }
	           		elseif ($request->has('first_name') && $request->first_name != "") {
	                    $instance->collection = $instance->collection->filter(function ($row) use ($request) {
	                 
	                        return Str::contains(strtolower($row['last_name']), strtolower($request->get('first_name'))) ? true : false;
	                    });
	                }
	           
	               
	            })->make(true);
		
    }
	
	public function viewhandrail(Request $request)
    {
    	$incidentid = $request->incidentid;
    	if(empty($incidentid)) return response()->json(['status'=>false, 'message'=>'Please provide Incident id'], 200);

    	try {
            $userData = \DB::table('cop_user_incidents_closed')->select('cop_user_incidents_closed.id','cop_user_incidents_closed.comment','cop_user_incidents_closed.created_at as closedate','cop_user_incidents_closed.reference as closereference','cop_incident_details.status','cop_incident_details.reference','cop_incident_details.updated_on','cop_incident_details.address','ref_user.first_name','ref_user.last_name','rfc.first_name as cop_first_name','rfc.last_name as cop_last_name', 'ref_incident_subcategory.sub_category_name','cop_incident_attachment.photo','cop_incident_attachment.video',
	            'cop_incident_details.incident_description')
			->join('cop_incident_details','cop_incident_details.id','=','cop_user_incidents_closed.cop_incident_details_id')
			->join('ref_incident_subcategory', 'ref_incident_subcategory.id', '=', 'cop_incident_details.ref_incident_subcategory_id')
			->join('ref_user','ref_user.id','=','cop_incident_details.created_by')
			->join('ref_user as rfc','rfc.id','=','cop_user_incidents_closed.created_by')
			->leftJoin('cop_incident_attachment','cop_incident_details.id','=','cop_incident_attachment.cop_incident_details_id')
			->where('cop_user_incidents_closed.id', $incidentid)
			->get(); 
           
            if($userData->isEmpty()) return response()->json(['status'=>false, 'message'=>'Something went wrong please try again later'], 200);
            
            return response()->json(['status'=>true, 'data'=>$userData], 200);
        } catch (QueryException $e) {
            return response()->json(['status'=>false, 'message'=>$e->getMessage()], 200);
        }
    }

    public function userdata(Request $request)
    {
    	 $users = User::where([['ref_user_type_id','=',UserType::_TYPE_OPERATOR],['approved','=',1]])->get(); // Approved User

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
                 
                        return Str::contains(strtolower($row['first_name']), strtolower($request->get('first_name'))) ? true : false;
                    });
                }
            })->make(true);
    }
	
	public function viewUser(Request $request)
	{
		$userid = $request->userid;
		
		if(empty($userid)) return response()->json(['status'=>false, 'message'=>'Please provide user id'], 200);
		
        try {
            $userData = User::where(array('id' => $userid))->get();            
            if($userData->isEmpty()) return response()->json(['status'=>false, 'message'=>'Something went wrong please try again later'], 200);
            
            foreach ($userData as $k => $v) 
            {
                $userData[$k]->first_name = ucfirst($v->first_name);
                $userData[$k]->last_name = ucfirst($v->last_name);
                $userData[$k]->profile_image = !empty($v->profile_image) ? asset('uploads/profile/'.$v->profile_image) : '';
                
                $userData[$k]->business_card1 = !empty($v->business_card1) ? asset('uploads/bussiness_cards/'.$v->business_card1) : '';
                $userData[$k]->business_card2 = !empty($v->business_card2) ? asset('uploads/bussiness_cards/'.$v->business_card2) : '';
                $userData[$k]->id_card1 = !empty($v->id_card1) ? asset('uploads/id_cards/'.$v->id_card1) : '';
                $userData[$k]->id_card2 = !empty($v->id_card2) ? asset('uploads/id_cards/'.$v->id_card2) : '';
                
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
        $users = User::where('ref_user_type_id','=',UserType::_TYPE_CITIZEN)->get();
        
        foreach ($users as $k => $v)
        {
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
                 
                        return Str::contains(strtolower($row['first_name']), strtolower($request->get('first_name'))) ? true : false;
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
	        $incidents = DB::table('cop_incident_details')->select('ref_incident_subcategory.sub_category_name',
	            'ref_user.first_name', 'ref_user.last_name', 'cop_incident_details.incident_description',
	            'cop_incident_details.other_description', 'cop_incident_details.reference', 'cop_incident_details.created_by',
	            'cop_incident_details.qr_code', 'cop_incident_details.latitude', 'cop_incident_details.longitude',
	            'cop_incident_details.address', 'cop_incident_details.city', 'cop_incident_details.created_at', 'cop_incident_details.id')
	            ->join('ref_incident_subcategory', 'ref_incident_subcategory.id', '=', 'cop_incident_details.ref_incident_subcategory_id')
	            ->join('ref_user', 'ref_user.id', '=', 'cop_incident_details.created_by')
	            ->orderBy('cop_incident_details.updated_at', 'DESC')->get();

	            if($request->input('lat') && $request->input('lng'))
	            {

	                $lat = $request->input('lat');
	                $lng = $request->input('lng');
	                $query = "SELECT ref_incident_subcategory.sub_category_name,
              	             ref_user.first_name,ref_user.last_name, cop_incident_details.incident_description,
             	             cop_incident_details.other_description, cop_incident_details.reference, cop_incident_details.created_by,
            	             cop_incident_details.qr_code, cop_incident_details.latitude, cop_incident_details.longitude,
            	             cop_incident_details.address, cop_incident_details.city, cop_incident_details.created_at, cop_incident_details.id, 
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
	        if(empty($request->input('user-id')))  return response()->json(['status'=>false, 'message'=>'Invalid request'], 200);
	        
	        $rs = User::where(['id'=>$request->input('user-id')])->update(['cops_grade'=>$request->input('grade')]);
	        if($rs) return response()->json(['status'=>true, 'message'=>'Grade updated successfully !'], 200);	        
	        return response()->json(['status'=>false], 200);
	    }
	    return response()->json(['status'=>false, 'message'=>'Invalid request'], 200);
	}
	
	public function updateaccount(Request $request)
	{
	    if($request->ajax())
	    {
	        # Validate if user id exists
	        if(empty($request->input('user-id')))  return response()->json(['status'=>false, 'message'=>'Invalid request'], 200);
	        
	        $rs = User::where(['id'=>$request->input('user-id')])->update(['status'=>$request->input('type')]);
	        $message = $request->input('type') == 0 ? "Account freezed successfully" : "Account unfreezed successfully";
	        if($rs) return response()->json(['status'=>true, 'message'=>$message], 200);
	        return response()->json(['status'=>false], 200);
	    }
	    return response()->json(['status'=>false, 'message'=>'Invalid request'], 200);
	}
	
	public function approveaccount(Request $request)
	{
	    if($request->ajax())
	    {
	        # Validate if user id exists
	        if(empty($request->input('user-id')))  return response()->json(['status'=>false, 'message'=>'Invalid request'], 200);
	        
	        $rs = User::where(['id'=>$request->input('user-id')])->update(['approved' => 1]);	        
	        if($rs) return response()->json(['status'=>true, 'message'=>'Account approved successfully'], 200);
	        return response()->json(['status'=>false], 200);
	    }
	    return response()->json(['status'=>false, 'message'=>'Invalid request'], 200);
	}
	
	public function refuseaccount(Request $request)
	{
	    $userId = Auth::user()->id;
	    
	    if($request->ajax())
	    {
	        # Validate if user id exists
	        if(empty($request->input('user-id')))  return response()->json(['status'=>false, 'message'=>'Invalid request'], 200);
	        
	        $rs = User::where(['id'=>$request->input('user-id')])->update(['approved' => 2]);
	        if($rs) 
	        {
	            # Account refused mark entry to cop approval comment table
	            CopApprovalComments::create([
	                'ref_user_id'=>$request->input('user-id'), 
	                'comment' => $request->input('refuse-text'), 
	                'created_by' => $userId
	            ]);
	            
	            return response()->json(['status'=>true, 'message'=>'Account rejected successfully'], 200);
	        }
	        return response()->json(['status'=>false], 200);
	    }
	    return response()->json(['status'=>false, 'message'=>'Invalid request'], 200);
	}
	
	public function assignintervention(Request $request)
	{
	    $userId = Auth::user()->id;
	    	    
	    if($request->ajax())
	    {
	        # Validate if user id exists	        
	        if(empty($request->input('objectId')) || empty($request->input('operators-id'))) return response()->json(['status'=>false, 'message'=>'Invalid request'], 200);
	        	
	        $rs = false;
	        foreach ($request->input('operators-id') as $o) 
	        {
	           $rs = CopUserIncidentTempMapping::create([
	               'ref_user_id' => $o, 
	               'cop_incident_details_id' => $request->input('objectId'), 
	               'ref_user_created_by' => $userId,
	               'ref_incident_status_id' => 1
	           ]);
	           
	        }
	        
	        if($rs)
	        {   
	            IncidentDetail::where('id', $request->input('objectId'))->update(['status'=>2]);
	            return response()->json(['status'=>true, 'message'=>'Intervention assigned successfully'], 200);
	        }
	        return response()->json(['status'=>false], 200);
	    }
	    return response()->json(['status'=>false, 'message'=>'Invalid request'], 200);
	}
}