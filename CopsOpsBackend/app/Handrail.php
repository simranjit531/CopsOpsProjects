<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Handrail extends Model
{
    protected $table = 'cop_handrail';

    protected $fillable = [
        'object', 'description', 'signature', 'reference', 'qr_code', 'updated_on', 'created_by', 'is_deleted'
    ];

    /**
     * The attributes that should be hidden for arrays.
     *
     * @var array
     */
    protected $hidden = [

    ];
}
