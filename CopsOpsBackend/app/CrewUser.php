<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class CrewUser extends Model
{
    protected $table = 'cop_crew_user_mapping';
    
    protected $fillable = [
        'cop_crew_id', 'ref_user_id', 'updated_by', 'is_deleted'
    ];
    
    /**
     * The attributes that should be hidden for arrays.
     *
     * @var array
     */
    protected $hidden = [
        
    ];
}
