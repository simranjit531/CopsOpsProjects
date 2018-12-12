<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App;
use Datatables;
use App\User;
use Illuminate\Support\Collection;
use Illuminate\Support\Str;

class BackendController extends Controller
{
    public function __construct()
    {
       
    }

    public function index()
    {
    	return view('backend.index');
    }

    public function usermanagement()
    {
    	return view('backend.usermanagement');
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
		 $users = User::where([['ref_user_type_id','=',1],['approved','=',2]])->get();
		 return Datatables::of($users)->removeColumn('user_password')
		 ->addColumn('view', function($userview){
				return '<a href="javascript:void(0)" id="viewCops" rel="'.$userview->id.'" data-toggle="modal" data-target="#myModal"><i class="fa fa-angle-right" aria-hidden="true"></i></a>';
			})->rawColumns(array("view"))->make(true);
	}
	
	public function validationRegTabledata(Request $request)
	{
		$users = User::where([['ref_user_type_id','=',1],['approved','=',0]])->get();
		 return Datatables::of($users)->removeColumn('user_password')
		 ->addColumn('view', function($userview){
				return '<a href="javascript:void(0)" id="viewCops" rel="'.$userview->id.'" data-toggle="modal" data-target="#myModal"><i class="fa fa-angle-right" aria-hidden="true"></i></a>';
			})->rawColumns(array("view"))->make(true);
	}
	
	public function archivedata(Request $request)
	{
		$users = User::where('ref_user_type_id','=',1)->get();
		 return Datatables::of($users)->removeColumn('user_password')
		 ->addColumn('view', function($userview){
				return '<a href="javascript:void(0)" id="viewCops" rel="'.$userview->id.'" data-toggle="modal" data-target="#myModal"><i class="fa fa-angle-right" aria-hidden="true"></i></a>';
			})->rawColumns(array("view"))->make(true);
	}

    public function userdata(Request $request)
    {
    	 $users = User::where([['ref_user_type_id','=',1],['approved','=',1]])->get(); // Approved User

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
		$userid= $request->userid;
		if(!empty($userid))
		{
		$userData= User::where(array('id' => $userid))->get();
		$data= $userData;
		echo json_encode($data);
          exit;
		}
		else
		{
		 $data = "Sorry!! Some thing Wrong!!";
          echo json_encode($data);
          exit;
		}
		
	}

    public function userdatacitizen(Request $request)
    {
         $users = User::where('ref_user_type_id','=',2)->get();

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
}