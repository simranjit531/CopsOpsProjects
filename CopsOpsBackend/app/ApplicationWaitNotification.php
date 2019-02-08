<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class ApplicationWaitNotification extends Model
{
    protected $table = 'cop_application_wait_notification';
    
    protected $fillable = [
        'ref_user_id', 'ref_incident_id', 'is_deleted'
    ];
    
    /**
     * The attributes that should be hidden for arrays.
     *
     * @var array
     */
    protected $hidden = [
        
    ];
}
