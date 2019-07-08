<?php

namespace App\Http\Controllers;

use Illuminate\Database\QueryException;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use App\User;
use Exception;

class LoginController extends Controller
{
    public function index()
    {
        try {
            return view('backend.auth.login');
        } catch (Exception $e) { 
            return view('404');
        }     
    }
    
    public function attempt_login(Request $request)
    {
        $request->validate([
            'email' => 'required|email|string',
            'password' => 'required|min:6|string',
        ]);
        
        $credentials = ['email_id' => $request->email, 'password' => $request->password, 'ref_user_type_id' => 5];        
        try
        {
            if(!Auth::attempt($credentials)){
                $request->session()->flash('message','Invalid credentials');
                return redirect()->route('login');
            }
            
            $user = Auth::user();
            return redirect()->route('dashboard');
            
        }
        catch (QueryException $qe)
        {
            dd($qe->getMessage());
        }        
    }    
    
    public function logout()
    {
        try {
            Auth::logout();
        } catch (Exception $e) {
        }
        
        
        return redirect()->route('login');
    }

    public function viewProfileData(Request $request, $userId)
    {
        \DB::enableQueryLog();
        $userData = User::where('user_id', $userId)->get();
        if(!$userData->isEmpty()) $userData = $userData->first();       
        return view('profile_view')->with(['userData'=>$userData]);
    }
}
