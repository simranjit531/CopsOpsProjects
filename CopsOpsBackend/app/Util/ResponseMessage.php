<?php

namespace App\Util;

class ResponseMessage
{
    const _STATUS_INVALID_REQUEST = 1;
    const _STATUS_DATA_NOT_FOUND = 2;
    const _STATUS_INVALID_JSON = 3;
    
    const _STATUS_REGISTRATION_SUCCESS = 1000;
    const _STATUS_REGISTRATION_FAILURE = 1001;

    const _STATUS_IMAGE_UPLOAD_SUCCESS = 2000;
    const _STATUS_IMAGE_UPLOAD_FAILURE = 2001;

    const _STATUS_INVALID_CREDENTIALS = 3000;

    const _STATUS_LOGIN_SUCCESS = 4000;

    const _STATUS_PASSWORD_RESET_SUCCESS = 5000;
    const _STATUS_PASSWORD_RESET_FAILURE = 5001;
    const _STATUS_EMAIL_NOT_FOUND = 5002;
    const _STATUS_OTP_VERIFIED_SUCCESS = 5003;
    const _STATUS_OTP_VERIFIED_FAILURE = 5004;
    const _STATUS_USER_NOT_FOUND = 5005;
    const _INVALID_USER_TYPE =5006;
    const _STATUS_INVALID_OPERATION =5007;
    const _STATUS_AVAILABILITY_SET_AVAILABLE = 5008;
    const _STATUS_AVAILABILITY_SET_UNAVAILABLE = 5009;

    const _STATUS_INCIDENT_ADD_SUCCESS = 6000;
    const _STATUS_INCIDENT_ADD_FAILURE = 6001;
    const _STATUS_HANDRAIL_ADD_SUCCESS = 6002;
    const _STATUS_HANDRAIL_ADD_FAILURE = 6003;
    const _STATUS_IMAGE_VIDEO_REQUIRED = 6004;
    const _STATUS_SIGNATURE_REQUIRED = 6005;
    const _STATUS_REGISTERED_INCIDENT_CLOSED_SUCCESS = 6006;
    const _STATUS_REGISTERED_INCIDENT_CLOSED_FAILURE = 6007;
    const _STATUS_NO_INCIDENT_REPORTED = 6008;
    
    const _STATUS_INTERVENTION_ASSIGNED_SUCCESS = 6009;
    const _STATUS_INTERVENTION_ASSIGNED_FAILURE = 6010;
    const _STATUS_INTERVENTION_ALREADY_ASSIGNED = 60011;
    const _STATUS_ACCOUNT_APPROVAL_PENDING = 60012;

    public static $response = [
        '1' => 'Invalid request, please check',
        '2' => 'Data not found !!!',
        '3' => 'Invalid JSON String',
        
//         '9999' => 'Invalid request, data not found, please check again',

        '1000' => 'Registration Successfull',
        '1001' => 'Unable to register, Please try again later !!!',

        '2000' => 'Image uploaded successfully',
        '2001' => 'Something went wrong, unable to upload image, please try again later',

        '3000' => 'Invalid Credentials',

        '4000' => 'Login Successful',

        '5000' => 'Password has been sent to your email successfully',
        '5001' => 'Unable to send password, please try again later',
        '5002' => 'No user exists with provided email address !!!',
        '5003' => 'OTP verified successfully !!!',
        '5004' => 'Invalid OTP, please try again later',
        '5005' => 'No user exists in our DB',
        '5006' => 'Invalid User Type',
        '5007' => "Invalid operation, please try again later",
        '5008' => "Availability status set to available",
        '5009' => "Availability status set to unavailable",

        '6000' => 'New incident added to the system successfully',
        '6001' => 'OOPS !!! Unable to add incident to the system, please try again later',
        '6002' => 'New handrail added to the system successfully',
        '6003' => 'OOPS !!! Unable to add handrail to the system, please try again later',
        '6004' => 'Image or video is required',
        '6005' => 'Signature required',
        '6006' => 'Incident closed successfully',
        '6007' => 'Something went wrong, unable to closed incident, please try again later',
        '6008' => 'No Incident reported at this location',
        '6009' => 'The intervention has been assigned to you',
        '6010' => 'Something went wrong, please try again',
        '60011' => 'This intervention has been already assigned.',
        '60012' => 'Account approval pending from backoffice.'
    ];

    public static function statusResponses($responseKey)
    {
        return static::$response[$responseKey];
    }
}