<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class HandrailAttachment extends Model
{
    protected $table = 'cop_handrail_attachment';

    protected $fillable = [
        'cop_handrail_id', 'photo', 'video', 'updated_on', 'is_deleted'
    ];

    /**
     * The attributes that should be hidden for arrays.
     *
     * @var array
     */
    protected $hidden = [

    ];
}
