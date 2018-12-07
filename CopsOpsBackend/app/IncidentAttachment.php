<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class IncidentAttachment extends Model
{
    protected $table = 'cop_incident_attachment';

    protected $fillable = [
        'cop_incident_details_id', 'photo', 'video', 'updated_on', 'is_deleted'
    ];

    /**
     * The attributes that should be hidden for arrays.
     *
     * @var array
     */
    protected $hidden = [

    ];
}
