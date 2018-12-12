<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class CopUserIncidentMapping extends Model
{
	protected $table = 'cop_user_incident_mapping';

    protected $fillable = [
        'ref_user_id', 'cop_incident_details_id', 'created_by','updated_at', 'created_at', 'is_deleted'
    ];
	
	/**
     * The attributes that should be hidden for arrays.
     *
     * @var array
     */
    protected $hidden = [

    ];
}
