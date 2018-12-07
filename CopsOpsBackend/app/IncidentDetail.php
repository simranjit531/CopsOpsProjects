<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class IncidentDetail extends Model
{
    protected $table = 'cop_incident_details';

    protected $fillable = [
        'ref_incident_category_id', 'ref_incident_subcategory_id', 'incident_description', 'other_description',
        'reference', 'qr_code', 'updated_on', 'created_by', 'is_deleted'
    ];

    /**
     * The attributes that should be hidden for arrays.
     *
     * @var array
     */
    protected $hidden = [

    ];
}
