<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class UserType extends Model
{
    const _TYPE_SUPER_ADMINISTRATOR = 1;
    const _TYPE_ADMINISTRATOR = 2;
    const _TYPE_OPERATOR = 3;
    const _TYPE_CITIZEN = 4;
    const _TYPE_BACKOFFICE = 5;   
    
    
    protected $table = 'ref_user_type';
    
    protected $fillable = [
        'user_type', 'is_deleted'
    ];
    
    /**
     * The attributes that should be hidden for arrays.
     *
     * @var array
     */
    protected $hidden = [
        
    ];
        
}
