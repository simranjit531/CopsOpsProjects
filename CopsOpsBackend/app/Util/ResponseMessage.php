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
    const _STATUS_REJECT_SUCCESS = 6015;
    const _STATUS_REJECT_FAILURE = 6016;
    
    const _STATUS_INTERVENTION_ASSIGNED_SUCCESS = 6009;
    const _STATUS_INTERVENTION_ASSIGNED_FAILURE = 6010;
    const _STATUS_INTERVENTION_ALREADY_ASSIGNED = 60011;
    const _STATUS_ACCOUNT_APPROVAL_PENDING = 60012;
    const _STATUS_ACCOUNT_APPROVAL_FREEZED = 60013;
    const _STATUS_ACCOUNT_APPROVAL_REFUSED = 60014;
    const _STATUS_PROFILE_IMAGE_REQUIRED = 60015;
    const _STATUS_INTERVENTION_CLOSE_ERROR = 60016;
    
    const _STATUS_NOTHING_TO_UPLOAD = 70001;
    const _STATUS_NEW_INTERVENTION_ASSIGNED = 70002;
    const _STATUS_NEW_MESSAGE_RECEIVED = 70003;

    const _STATUS_PROFILE_UPDATE_SUCCESS = 80001;
    const _STATUS_PROFILE_UPDATE_FAILURE = 80002;
    const _STATUS_PASSWORD_CHANGE_SUCCESS = 80003;
    const _STATUS_PASSWORD_CHANGE_FAILURE = 80004;
    const _STATUS_MEDICAL_DATA_SUCCESS = 80005;
    const _STATUS_MEDICAL_DATA_FAILURE = 80006;

    const _STATUS_OLD_PASSWORD_NOT_MATCH = 80007;
    
    public static $lang = 'en';

    public static $response = [
        '1' => 'Invalid request, please check',
        '2' => 'Data not found !!!',
        '3' => 'Invalid JSON String',
        
//         '9999' => 'Invalid request, data not found, please check again',

        '1000' => 'Registration Successfully',
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
        '60011' => 'The intervention has been already assigned.',
        '60012' => 'Account approval pending from backoffice.',
        '60013' => 'Your account has been freeze. Please contact Backoffice Administrator.',
        '60014' => 'Account refused from backoffice.',
        '60015' => 'Profile Image is required !',
        '60016' => 'You can only close interventions assigned to you only',
        '6015' => 'Rejected Successful',
        '6016' => 'Rejected Failed',
        
        '70001' => 'Nothing to upload, please try again later',
        '70002' => 'A new intervention has been assigned to you',
        '70003' => 'You have received a new message',

        '80001' => 'Your profile is updated successfully',
        '80002' => 'Something went wrong, unable to update your profile',
        '80003' => 'Your password has been changed successfully',
        '80004' => 'Something went wrong, unable to change your password',
        '80005' => 'Your medical information is updated successfully',
        '80006' => 'Something went wrong, we are unable to update your medical information.',
        '80007' => 'Old password does not match, please check and try again.'
    ];
    
    public static $responseFrench = [
        '1' => 'Votre demande n’est pas valide, veuillez verifier',
        '2' => 'Aucune information n’a été retrouvée',
        '3' => 'Le Fils J-son est Invalide',
        
        //         '9999' => 'Invalid request, data not found, please check again',
        
        '1000' => 'Inscris avec succées',
        '1001' => 'Impossible de s’inscrire, Veuillez réessayer!!!',
        
        '2000' => 'Image téléchargée avec succès',
        '2001' => 'Un souci est survenu, incapable de télécharger l’image, Veuillez reessayer plus tard',
        
        '3000' => 'Identifiants Invalids',
        
        '4000' => 'Connexion réussie',
        
        '5000' => 'Le mot de passe a été envoyé sur votre adresse couriel avec succès.',
        '5001' => 'Impossible d’envoyer le mot de passe, veuillez réessayer plus tard',
        '5002' => 'Aucun utilisateur existant sous cette adresse courriel !!!',
        '5003' => 'l’OTP à été vérifié avec succès',
        '5004' => 'Invalide OTP, veuillez réessayer plus tard',
        '5005' => 'Aucun utilisateur existant dans notre DB',
        '5006' => 'Type d’utilisateur invalide',
        '5007' => "Opération invalide, veuillez réessayer plus tard",
        '5008' => "Statut de disponibilité est à  disponible",
        '5009' => "Statut de disponibilité défini sur disponible",
        
        '6000' => 'Un nouvel incident a été ajouter  au système avec success',
        '6001' => 'OOPS!! ! Impossible d’ajouter un nouvel incident au système, Veuillez réessayer  plus tard',
        '6002' => 'une main courante a été ajoutée au système avec succès',
        '6003' => 'OOPS!!! Impossible d’ajouter une main courante au système, veuillez réessayer plus tard',
        '6004' => 'Une vidéo ou une image est requise',
        '6005' => 'Une Signature est requise',
        '6006' => 'L’incident est cloturé avec succé',
        '6007' => 'Un problème est survenue, impossible de fermer l\'incident, veuillez réessayer plus tard.',
        '6008' => 'Aucun incident n’a été signalé à cet endroit',
        '6009' => 'L\'intervention vous a été assignée',
        '6010' => ' Une erreur s\'est produite. Veuillez réessayer',
        '60011' => 'L\'interevention Vous a été assigner',
        '60012' => 'L’acceptation du compte en attente du backoffice.',
        '60013' => 'Votre compte a été bloquer. Veuillez contacter l\'administrateur du Backoffice.',
        '60014' => 'Le compte a été refuse depuis le Backoffice',
        '60015' => 'Une photo de profil est requise',
        '60016' => 'Vous ne pouvez fermer que les interventions qui vous ont été assignées',
        '6015' => 'Rejeter avec succé',
        '6016' => 'Echeque de rejet',
        
        '70001' => 'Rien � t�l�charger, veuillez r�essayer plus tard',
        '70002' => 'Une nouvelle intervention vous a été assignée',
        '70003' => 'Vous avez reçu un nouveau message',
        
        '80001' => 'Votre profil est mis à jour avec succès',
        '80002' => 'Quelque chose s\'est mal passé, impossible de mettre à jour votre profil',
        '80003' => 'Votre mot de passe a été changé avec succès',
        '80004' => 'Quelque chose s\'est mal passé, impossible de changer votre mot de passe',
        '80005' => 'Vos informations médicales sont mises à jour avec succès',
        '80006' => 'Quelque chose s\'est mal passé, nous ne pouvons pas mettre à jour vos informations médicales.',

        '80007' => 'Ancien mot de passe ne correspond pas, veuillez vérifier et réessayer.'
    ];

    public static function statusResponses($responseKey, $lang=null)
    {
        if($lang == "fr") return static::$responseFrench[$responseKey];
        return static::$response[$responseKey];
    }
}