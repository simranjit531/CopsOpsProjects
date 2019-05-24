<?php

return [

    /*
    |--------------------------------------------------------------------------
    | Pages Reset Language Lines
    |--------------------------------------------------------------------------
    |
    | The following language lines are the default lines which match reasons
    | that are given by the Pages broker for a Pages update attempt
    | has failed, such as for an invalid token or invalid new Pages.
    |
    */
	  'usermgnt' => [
            'listofoperators' => 'list des operateurs',
			'listofcitizens' => 'list des citoyens',
			'citizentools' => 'Outils citoyens',
			'Operatorstools' => 'Outils opérateur',
			'listofusers' => 'Liste des utilisteurs',
			'dailycrew' => 'Equipage journalier',
			'validationofregistrants' => 'Validations des inscrits',
			'accountrefuses' => 'Compte refuse',
			'enternameoperator' => 'Enter le nom d’un Opérateur',
			'enternamecitizen' => 'Entrer le nom d’un Citoyen',
			'assignanintervention' => 'Assigner une intervention',
			'operator' => 'Opérateur',
			'object' => 'objet',
			'descriptionoftheintervention' => 'Description de l’interevention',
			'address' => 'Adresse',
			'assign' => 'Assigner',
			'tables' =>[
			'firstname' => 'Nom',
			'lastname' => 'Prénom',
			'email' => 'Adresse mail',
			'grade' => 'Grade',
			'birthdate' => 'Date de naissance',
			'numberofreports' => 'Nombre de signalement',
			'number' => 'Numéro de téléphone', 
			],
			'intervention' => 'Intervention',
			'attributed' => 'attribués',
			'totelinterventionfenced' => 'Total intereventions cloturées',
	        'freeze_account' => 'Figer le compte',
	        'change_access' => 'Modifier le grade d’accées',
			'assigned_incidents' => 'Assigned Incidents',
			
			
       ],
	   'archive' =>[
		'reportlog' => 'Journal des signalements',
		'currenthand' => 'Main Courante',
		'INTERVENTIONSATTRIBUEES' =>'Interventions attribuées',
		'interventionAddress' => 'Intervention Address',
		'status' => 'Etat',
		'generalInformation' => 'Information générale',
		'report' => 'Rapport',
		],
    'copops' => 'COP OPS',
	'fromParis' => 'de Paris',
	'administrator' => 'Administrateur',
    'dashboard' => 'DASHBOARD',
	'controlCenter' => 'Centre de controle',
	'userManagement' => 'Gestion des utilisateurs',
	'discussion' => 'Discussion',
	'archiveCenter' => 'Centre d’archives',
	'cardTools' => 'OUTILS CARTE',
	'logout' => 'Déconnexion',
	'home' => 'Accueil',
	'PORTE' => 'Recherche',
	'SELECT' => 'Sélectionner',
	'Citizen' => 'Citoyen',
	'Operator' => 'Opérateur',
	'ZoneofInterest'=>'Zone d’intérêt',
	'PointofInterest'=>'Point d’intérêt',
	'save' => 'Enregistrer',
	'datatime' => 'Heure/ Date',
	'Reporter' => 'Signalant',
	'Subject' => 'Motif',
	'other' => 'Autres',
	'Previous' => 'Previous',
	'Next' => 'Next',
	'Validator' => 'Valider',
	'copyright'=> 'Droit d’auteur © 2018-19 Copops Tous les droits sont réservés',
	'selectregdate'=>'Sélectionner une date d’inscription',
	'nodata'=> 'Aucune information disponible dans le tableau',
	'welcomecopopschat'=>'Bienvenue sur le support de chat de Copops!',
	'startchating'=>'Veuillez sélectionner un contact pour commencer la discussion',
	'fromdate'=>'De la date',
	'todate'=>'A la date',
	'Actual' => 'Actuel',
	'Reference' => 'Référence',
	'Attachement'=>'Pièces Jointes',
	'State' => 'Etat',
	'Finished' =>'Terminé',
	'Onwait' =>'En attente',
	'Pending' =>'En cours',
	'createteam' => 'Créer une équipe',
	'namecrew' => 'Nom de l’équie',
	'teamname' => 'Nom de l’équipe',
	'Effective' => 'Effectif',
	'Addnew' => 'Ajouter un nouvel opérateur',
	'create' => 'Créer',
	'Loading' => 'Patienter',
	'addmore' => 'AJOUTER PLUS',
	'inforeport' => 'Information de l’incident',
	'otherinfo'=> 'Autres informations',
	'description' => 'déscription',
	'Subjectofthereport' => 'Motif de l’incident',
	'Assigned'=>'Interventions assignées',
	'Completed' => 'Intervention cloturées',
	'Fireman'=>'Pompier',
	'City' => 'Ville',
	'handrail' => 'Main Courante',
	'language' => 'Langues',
	'reporting' => 'Signalements',
	'Close' => 'Fermer',
	'interventionassigned' => 'Interventions Assignées',
	'CrewCreatedSuccessfully' => 'L’équipe a été créé avec sucée',
	'OOPS' => 'OUPS !!!',
	'Invalidrequest' => 'Requête invalide, veuillez réessayer plus tard',
	'somethingwentwrong' => 'Un problème est survenue, veuillez réessayer plus tard',
	'pleaseprovideincidentid' => 'Veuillez entrer l’identifiant de l’incident',
	'Pleaseprovideuserid' => ' Veuillez entrer l’identifiant de l’utilisateur',
	'freezedsuccess'=>'Le compte a été bloquer avec sucée',
	'unfreezedsuccess' => 'Le compte a été débloquer avec sucée',
	'accountapproved' => 'Le compte été approuvé avec sucée',
	'accountreject' => 'le compte a été rejeter avec sucée',
	'interventionassigned'=>'L’intervention a été assigner avec sucée',
	/* index.php view */
	'areusureremovepoi' => 'Etes- vous sure de vouloir supprimer ce point d’intérêt',
	'giocodewasnotsuccess' => 'Le Géocode ne répond paspour la raison suivante:',
	'dousaveinfo' => 'Voulez-vous sauvegarder l’information de la carte que cette action annulera votre dernière modification?',
	'doudiscard' => 'Vous voulez supprimer les information enregistrées de la carte',
	
	/* validationofregistants */
	'areusurerefuse'=>'Etes-vous sure de vouloir rejeter ce compte d’utilisateur?',
	
	/* dailycrew */
	'morethenonecrew'=> 'Veuillez sélectionner plus du membre pour créer une équipe',
	
	/*master*/
	'browsernotsuppory' => 'Votre navigateur ne supporte pas l’élément audio',
	
	'research' => 'Recherche',
	'writemessage'=>'Écrire un message',
	'chatheader' => 'Messagerie',
	'frenchme' => 'Moi'
];
