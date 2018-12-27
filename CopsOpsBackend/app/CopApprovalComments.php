<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class CopApprovalComments extends Model
{
    protected $table = 'cop_approval_comments';
    
    protected $fillable = [
        'ref_user_id', 'comment', 'created_by'
    ];
    
    /**
     * The attributes that should be hidden for arrays.
     *
     * @var array
     */
    protected $hidden = [
        
    ];
}
