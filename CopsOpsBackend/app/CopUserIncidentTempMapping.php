<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class CopUserIncidentTempMapping extends Model
{
    protected $table = 'cop_user_incident_temp_mapping';
    
    protected $fillable = [
        'ref_user_id', 'cop_incident_details_id', 'ref_user_created_by','updated_at', 'created_at', 'is_deleted', 'ref_incident_status_id'
    ];
    
    /**
     * The attributes that should be hidden for arrays.
     *
     * @var array
     */
    protected $hidden = [
        
    ];
}
