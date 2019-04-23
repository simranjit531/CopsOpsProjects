<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Chat extends Model
{
    protected $table = 'copops_chat';
    
    protected $fillable = [
        'sender_id', 'receiver_id', 'message', 'message_type', 'is_read', 'is_deleted'
    ];
}
