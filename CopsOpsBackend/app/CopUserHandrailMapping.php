<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class CopUserHandrailMapping extends Model
{
    protected $table = 'cop_user_handrail_mapping';

    protected $fillable = [
        'ref_user_id', 'cop_handrail_id', 'created_by','updated_at', 'created_at', 'is_deleted'
    ];
	
	/**
     * The attributes that should be hidden for arrays.
     *
     * @var array
     */
    protected $hidden = [

    ];
}
