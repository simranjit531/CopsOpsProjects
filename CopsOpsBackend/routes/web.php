<?php

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/
Route::get('lang/{lang}', 'LanguageController@swap')->name('lang');

# Login Route
Route::get('/', 'LoginController@index');
Route::get('/login', 'LoginController@index')->name('login');
Route::post('/attempt/login', 'LoginController@attempt_login')->name('login.attempt');
Route::get('/logout', 'LoginController@logout')->name('logout');


Route::get('/dashboard', 'BackendController@index')->name('dashboard');
Route::get('/usermanagement', 'BackendController@usermanagement');
Route::get('/dailycrew', 'BackendController@dailycrew');
Route::get('/validationofregistrants', 'BackendController@validationofregistrants');
Route::get('/accountrefuses', 'BackendController@accountrefuses');
Route::get('/reduseTabledata', 'BackendController@reduseTabledata');
Route::get('/validationRegTabledata', 'BackendController@validationRegTabledata');


Route::get('/userdata', 'BackendController@userdata');
Route::get('/userdatacitizen','BackendController@userdatacitizen');
Route::get('/archivecenter', 'BackendController@archivecenter');
Route::get('/archivedata', 'BackendController@archivedata');
Route::post('/viewincident','BackendController@viewincident'); 
Route::get('/currenthanddata','BackendController@currenthanddata'); 
Route::post('/viewhandrail','BackendController@viewhandrail'); 

Route::get('/currenthand', 'BackendController@currenthand');
Route::get('/chat', 'BackendController@chat');

Route::post('/viewuser','BackendController@viewUser');

// Auth::routes();

Route::get('/home', 'HomeController@index')->name('home');


/* Get list of incidents for home page */

Route::post('/list/incidents', 'BackendController@listofincidents')->name('backoffice.incidents.list');
Route::post('/incident/data', 'BackendController@incidentdetail')->name('backoffice.incident.detail');

# Grade update
Route::post('grade/update', 'BackendController@updategrade')->name('backoffice.grade.update');
Route::post('account/update', 'BackendController@updateaccount')->name('backoffice.account.update');

# Operator account approve
Route::post('account/approve', 'BackendController@approveaccount')->name('backoffice.account.approve');

# Operator account refuse
Route::post('account/refuse', 'BackendController@refuseaccount')->name('backoffice.account.refuse');

# Assing intervention
Route::post('intervention/assign', 'BackendController@assignintervention')->name('backoffice.intervetnion.assign');