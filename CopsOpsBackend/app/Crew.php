<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Crew extends Model
{    
    protected $table = 'cop_crew';
    
    protected $fillable = [
        'crew_name', 'incident_handrail', 'cop_incident_details_id', 'cop_handrail_id', 'created_at', 'updated_by', 'is_deleted'
    ];
    
    /**
     * The attributes that should be hidden for arrays.
     *
     * @var array
     */
    protected $hidden = [
        
    ];
    
    public function get_crew_members()
    {
        return $this->hasMany(CrewUser::class, 'cop_crew_id', 'id');
    }
}
