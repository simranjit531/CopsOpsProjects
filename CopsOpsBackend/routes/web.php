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
Route::get('/userdata', 'BackendController@userdata');
Route::get('/userdatacitizen','BackendController@userdatacitizen');
Route::get('/archivecenter', 'BackendController@archivecenter');
Route::get('/chat', 'BackendController@chat');
