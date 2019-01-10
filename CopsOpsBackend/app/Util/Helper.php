<?php
/**
 * Created by PhpStorm.
 * User: kailash.karayat
 * Date: 12/3/2018
 * Time: 12:33 PM
 */

function generateRandomString($length = 10) {
    $characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
    $charactersLength = strlen($characters);
    $randomString = '';
    for ($i = 0; $i < $length; $i++) {
        $randomString .= $characters[rand(0, $charactersLength - 1)];
    }
    return $randomString;
}

function has_keys(array $needle, array $haystack){
    $array = array();
    foreach ($needle as $n){
        if(!in_array($n, $haystack)) array_push($array,1);
    }
    return $array;
}

function get_address_city($lat, $lng)
{
    $key = 'AIzaSyBrxD_Azxlj-PY4MUR1RYzwIFuLFt0GnOM';
    $curl = curl_init();
    curl_setopt_array($curl, array(
        CURLOPT_RETURNTRANSFER => 1,
        CURLOPT_URL => "https://maps.googleapis.com/maps/api/geocode/json?latlng=$lat,$lng&key=$key",
    ));
    $resp = curl_exec($curl);
    $respArr = json_decode($resp, true);
    if($respArr === null) return array('status'=>false);
    return $respArr;

    curl_close($curl);
}

// if (! function_exists('generate_unique_id')) {
       
//     function generate_unique_id($prefix)
//     {
//         return \Illuminate\Support\Str::words($value, $words, $end);
//     }
// }

if (! function_exists('words')) {
    /**
     * Limit the number of words in a string.
     *
     * @param  string  $value
     * @param  int     $words
     * @param  string  $end
     * @return string
     */
    function words($value, $words = 100, $end = '...')
    {
        return \Illuminate\Support\Str::words($value, $words, $end);
    }
}


if(! function_exists('_execute_curl_request')){
    
    function _execute_curl_request($url, $post){
        try {
            
            // Configure cURL
            $curl = curl_init();
            
            curl_setopt($curl, CURLOPT_URL, $url);
            curl_setopt($curl, CURLOPT_POST, true); // Use POST
            curl_setopt($curl, CURLOPT_POSTFIELDS, $post); // Setup post body
            curl_setopt($curl, CURLOPT_RETURNTRANSFER, true); // Receive server response
            curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, false); // Receive server response
            
            // Execute request and read response
            $response = curl_exec($curl);
            
            // Close connection
            curl_close($curl);
            
            // Check errors
            if ($response) return array('flag'=>1, 'result'=>json_decode($response));
            else return false;
            
        } catch (Exception $e) {
            return array(['flag'=>0, 'message'=>$e->getMessage()]);
        }
    }
    
}

if(!function_exists('_quickblox_create_session')){
    
    function _quickblox_create_session(){
                
        $nonce = rand();
        $timestamp = time();
        $signature_string = "application_id=" . env('QUICKBLOX_APPLICATION_ID') . "&auth_key=" . env('QUICKBLOX_AUTH_KEY') . "&nonce=" . $nonce . "&timestamp=" . $timestamp . "&user[login]=" . env('QUICKBLOX_USER_LOGIN') . "&user[password]=" . env('QUICKBLOX_USER_PASSWORD');
        
        $signature = hash_hmac('sha1', $signature_string, env('QUICKBLOX_AUTH_SECRET'));
        
        // Build post body
        $post = http_build_query(array(
            'application_id' => env('QUICKBLOX_APPLICATION_ID'),
            'auth_key' => env('QUICKBLOX_AUTH_KEY'),
            'timestamp' => $timestamp,
            'nonce' => $nonce,
            'signature' => $signature,
            'user[login]' => env('QUICKBLOX_USER_LOGIN'),
            'user[password]' => env('QUICKBLOX_USER_PASSWORD')
        ));
                
        $url = env('QUICKBLOX_API_ENDPOINT').'/session.json';
        return _execute_curl_request($url, $post);
    }
}

// TODO
if(!function_exists('_quickblox_create_user')){
    
    function _quickblox_create_user($data){
        
        $post = http_build_query(array(
            'user[login]' => $data['username'],
            'user[password]' => $data['password'],
            'user[email]' => $data['email'],
            'user[full_name]' => $data['fullname'], 
            'user[tag_list]' => $data['tag_list'] 
        ));
        
        $curl = curl_init();
        curl_setopt($curl, CURLOPT_URL, env('QUICKBLOX_API_ENDPOINT').'/users.json');
        curl_setopt($curl, CURLOPT_POST, true);
        curl_setopt($curl, CURLOPT_POSTFIELDS, $post);
        curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($curl, CURLOPT_HTTPHEADER, array(
            'Accept: application/json',
            'Content-Type: application/x-www-form-urlencoded',
            'QuickBlox-REST-API-Version: 0.1.0',
            'QB-Token: ' . $data['token']
        ));
        
        $response = curl_exec($curl);
        if ($response) return $response;
        else return false;
        
        curl_close($curl);        
    }
}