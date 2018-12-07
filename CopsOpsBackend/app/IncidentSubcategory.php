<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class IncidentSubcategory extends Model
{
    protected $table = 'ref_incident_subcategory';

    protected $fillable = [
        'ref_incident_category_id', 'sub_category_name', 'description', 'icon', 'is_deleted'
    ];

    /**
     * The attributes that should be hidden for arrays.
     *
     * @var array
     */
    protected $hidden = [

    ];
}
