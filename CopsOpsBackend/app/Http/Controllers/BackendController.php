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

    public function userdata(Request $request)
    {
    	 $users = User::where('ref_user_type_id','=',2)->get();

        return Datatables::of($users)->addColumn('action', function ($user) {
                return '<div class="radio"><label><input type="radio" name="optradio"></label></div>';
            })->removeColumn('user_password')->filter(function ($instance) use ($request) {
                if ($request->has('first_name') && $request->first_name != "") {
                    $instance->collection = $instance->collection->filter(function ($row) use ($request) {
                 
                        return Str::contains(strtolower($row['first_name']), strtolower($request->get('first_name'))) ? true : false;
                    });
                }
            })->make(true);
    }

    public function userdatacitizen(Request $request)
    {
         $users = User::where('ref_user_type_id','=',1)->get();

        return Datatables::of($users)->addColumn('action', function ($user) {
                return '<div class="radio"><label><input type="radio" name="optradio"></label></div>';
            })->removeColumn('user_password')->filter(function ($instance) use ($request) {
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