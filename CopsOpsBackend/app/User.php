<?php

namespace App;

use Illuminate\Notifications\Notifiable;
use Illuminate\Contracts\Auth\MustVerifyEmail;
use Illuminate\Foundation\Auth\User as Authenticatable;

class User extends Authenticatable implements \Illuminate\Contracts\Auth\Authenticatable
{
    use Notifiable;

    /**
     * The attributes that are mass assignable.
     *
     * @var array
     */
    protected $table = 'ref_user';

    protected $fillable = [
            'user_id', 'ref_user_type_id', 'user_password', 'first_name', 'last_name', 'date_of_birth', 'place_of_birth',
            'gender',  'phone_number', 'email_id', 'id_card1', 'id_card2', 'business_card1', 'business_card2',
            'latitude', 'longitude', 'otp', 'verified', 'approved', 'cops_grade', 'available', 'is_deleted',
            'profile_image', 'profile_qrcode'
    ];

    /**
     * The attributes that should be hidden for arrays.
     *
     * @var array
     */
    protected $hidden = [
        'user_password', 'remember_token',
    ];
    
    //protected $userPassword = 'user_password';
    
    public function getAuthPassword() {
        return $this->user_password;
    }
        
    public function getReporterType()
    {
        return $this->hasOne(UserType::class, 'id', 'ref_user_type_id');
    }
}
