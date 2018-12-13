<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class CopUserIncidentClosed extends Model
{
    protected $table = 'cop_user_incidents_closed';

    protected $fillable = [
        'cop_incident_details_id', 'comment', 'created_by','updated_at', 'created_at', 'ref_incident_status_id',
        'is_deleted'
    ];

    /**
     * The attributes that should be hidden for arrays.
     *
     * @var array
     */
    protected $hidden = [

    ];
}
