<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Notification extends Model
{
    protected $table = 'cop_notification';
    
    protected $fillable = [
        'table_id', 'table', 'message', 'status', 'is_deleted'
    ];
    
    /**
     * The attributes that should be hidden for arrays.
     *
     * @var array
     */
    protected $hidden = [ 
        
    ];
}
