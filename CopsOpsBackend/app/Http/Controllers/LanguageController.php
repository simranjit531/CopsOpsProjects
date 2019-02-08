<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App;
use Session;

class LanguageController extends Controller
{
    /**
     * @param $lang
     *
     * @return \Illuminate\Http\RedirectResponse
     */
    public function swap($lang)
    {
        Session::put('locale', $lang);
        return redirect()->back();
    }
}
