<?php

use Illuminate\Http\Request;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

#Route::middleware('auth')->post('/signup', 'ApiController@register');

Route::group(['prefix' => 'auth'], function () {

    Route::post('/incidents', 'API\ApiController@get_incidents');
    Route::post('/sub/incidents', 'API\ApiController@get_incident_subcategory');

    Route::post('register/incident',  'API\ApiController@report_incident');
    Route::post('register/handrail',  'API\ApiController@report_handrail');

    Route::post('/register', 'API\ApiController@register');
    Route::post('/validate/otp', 'API\ApiController@validate_otp');

    Route::post('/login', 'API\ApiController@login');
    Route::post('/password/reset', 'API\ApiController@reset_password');


    Route::post('/upload/image', 'API\ApiController@upload_image');
    Route::post('/test/test', 'API\ApiController@parseJSON');

    Route::post('/incident/list', 'API\ApiController@get_incident_list');
    Route::post('/incident/city/list', 'API\ApiController@get_incident_by_city');

    Route::post('/copincident/list', 'API\ApiController@get_cops_incident_list');
	Route::post('/copincident/status/list', 'API\ApiController@get_cops_incident_all_list');

    Route::post('/profile/attributes', 'API\ApiController@get_profile_attributes');
    Route::post('/profile/set/availability', 'API\ApiController@set_available_status');
    Route::post('/profile/set/locations', 'API\ApiController@store_lat_lng');
    
    
    Route::post('/registered/incident/assigned', 'API\ApiController@assigned_intervention');
    Route::post('/registered/incident/intervent', 'API\ApiController@assign_intervention');
    Route::post('/registered/incident/close', 'API\ApiController@close_registered_incident');
	
	Route::post('/incident/rejected','API\ApiController@reject_incident'); //pp
});

//Route::middleware('auth:api')->get('/user', function (Request $request) {
//    return $request->user();
//});
