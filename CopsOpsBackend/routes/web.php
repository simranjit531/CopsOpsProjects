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

Route::get('/', 'BackendController@index');
Route::get('/dashboard', 'BackendController@index');
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

Route::get('/currenthand', 'BackendController@currenthand');
Route::get('/chat', 'BackendController@chat');

Route::post('/viewuser','BackendController@viewUser');
