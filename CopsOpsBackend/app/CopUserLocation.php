<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class CopUserLocation extends Model
{
    protected $table = 'cop_user_locations';
    
    protected $fillable = [
        'user_id', 'latitude', 'longitude'
    ];
    
    /**
     * The attributes that should be hidden for arrays.
     *
     * @var array
     */
    protected $hidden = [
        
    ];
}
