<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class IncidentCategory extends Model
{
    protected $table = 'ref_incident_category';

    protected $fillable = [
        'category_name', 'description', 'icon', 'help_line_no', 'is_deleted'
    ];

    /**
     * The attributes that should be hidden for arrays.
     *
     * @var array
     */
    protected $hidden = [

    ];
}
