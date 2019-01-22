<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class CopBackofficeMapInteraction extends Model
{
    protected $table = 'cop_backoffice_map_interaction';
    
    protected $fillable = [
        'circle_lat', 'circle_lng', 'circle_radius', 'pin_lat', 'pin_lng', 'map_zoom', 'map_lat', 'map_lng', 'created_by', 'is_deleted'
    ];
    
    /**
     * The attributes that should be hidden for arrays.
     *
     * @var array
     */
    protected $hidden = [
        
    ];
}
