<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class UserDeviceMapping extends Model
{
    protected $table = 'cop_user_device_mapping';

    protected $fillable = [
        'ref_user_id', 'device_id', 'created_on	', 'is_deleted'
    ];

    /**
     * The attributes that should be hidden for arrays.
     *
     * @var array
     */
    protected $hidden = [

    ];
}
