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