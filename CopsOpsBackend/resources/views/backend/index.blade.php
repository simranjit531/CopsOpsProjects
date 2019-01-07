@extends('backend.layouts.backendapp') 
@section('content')

<!-- Content Header (Page header) -->
<div class="content-header">
	<div class="container-fluid">
		<div class="row mb-2">
			<div class="col-sm-6">
				<h1 class="m-0 text-dark">{{ trans('pages.controlCenter') }}</h1>
			</div>
			<!-- /.col -->
			<div class="col-sm-6">
				<ol class="breadcrumb float-sm-right">
					<li class="breadcrumb-item"><a href="#">Home</a></li>
					<li class="breadcrumb-item active">{{ trans('pages.controlCenter')
						}}</li>
						
					<input type="hidden" name="hidden_user_id" value="{{ Auth::user()->user_id }}"/>
                    <input type="hidden" name="hidden_user_full_name" value="{{ Auth::user()->first_name.' '.Auth::user()->last_name }}"/>
                    <input type="hidden" name="hidden_usergroup" value="copops"/>
                    <input type="hidden" name="hidden_userpassword" value="webAppPass"/>
                    <input type="hidden" name="hidden_email" value="{{ Auth::user()->email_id }}"/>
				</ol>
			</div>


			<div class="round-right-part mb-3">
				<!-- SEARCH FORM -->
				<form class="form-inline col-md-8 ml-3 mt-1 float-left">
					<div class="input-group input-group-sm">
						<div class="input-group-append">
							<button class="btn btn-navbar" type="button">
								<i class="fa fa-search"></i>
							</button>
						</div>
						<input class="form-control form-control-navbar" type="search"
							placeholder="Porte" aria-label="Search" id="search_places">

					</div>

					<div class="col-12 col-sm-2 col-md-2 ml-3 select-box">
						<select name="user_type" id="user_type">
							<option value="" selected>--Select--</option>
							<option value="4">Citizen </option>
							<option value="3">Operator</option>
							<option value="">Zone of Interest</option>
							<option value="">Point of Interest</option>
						</select>
					</div>

					<div class="col-12 col-sm-4 col-md-4 location-zone ml-3">
						<ul>
							<li><a href="javascript:void(0);" id="add-circle">Zone <br>d'internet <i class="fa fa-map-marker"
									aria-hidden="true"></i></a></li>
							<li><a href="javascript:void(0);" id="remove-circle">Point <br>d'internet <i class="fa fa-arrows-v"
									aria-hidden="true"></i></a></li>
						</ul>
					</div>

				</form>
			</div>

			<!-- /.col -->


			<div class="col-sm-12 dashboard-map mb-1">			
				<div id="map" style="width:100%;height:400px;"></div>
			</div>


			<div class="col-sm-12 journal-table mt-2">
				<div class="row">
					<div class="table-format col-sm-9">
						<h2>
							Journal signalments &amp; intervenants <i
								class="fa fa-map-marker" aria-hidden="true"></i>
						</h2>
						<table class="table table-bordered table-striped" id="datatables">
							<input type="hidden" name="hidden_lat"/>
							<input type="hidden" name="hidden_lng"/>
							<thead>
								<tr>
									<th>Date</th>
									<th>Address</th>
									<th>Reporter</th>
									<th>First/Last Name</th>
									<th>Subject</th>
									<th>Description</th>
									<th>Status</th>
								</tr>
							</thead>
							<tbody></tbody>
						</table>
					</div>
					<div class="col-sm-3 mini-selection chat-right-admin float-right">
						<h2>Live Chat</h2>
						<div class="chat-wrapper">
						<div class="chat-header">
							<div class="row">
							<div class="col-md-2"><i class="fa fa-address-book-o" aria-hidden="true"></i></div>
							<div class="col-md-8"><div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-search" aria-hidden="true"></i></span>
                                    <input type="text" name="search" class="form-control">
                                </div>
                            </div>
							<div class="col-md-2"><i class="fa fa-comments-o" aria-hidden="true"></i></div>
							</div>
						</div>
						<div class="chat-inner-section"></div>
						</div>
					</div>
				</div>
			</div>

			<!--2Jan pp-->
				<!-- Modal -->
	<div id="myModal" class="modal fade" role="dialog">
		<div class="modal-dialog">

			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>

				</div>
				<div class="modal-body">
					<div
						class="col-12 col-sm-12 col-md-12 col-lg-12 col--xl-12 user-mgm-m7">
						<h6></h6>

						<div class="row">

							<div class="col-12 col-sm-2 col-md-2 col-lg-2 col-xl-2 left-part">
								<figure>
									<img id="profile_image" src="{{asset('img/jean-img.jpg')}}" alt="jean-img">
								</figure>
								<h2>
									<span id="cop_grade"></span>
								</h2>
							</div>

							<div
								class="col-12 col-sm-10 col-md-10 col-lg-10 col-xl-10 right-part">
								<ul>
									<li><b>{{ trans('pages.usermgnt.tables.firstname')}} / {{
										trans('pages.usermgnt.tables.lastname')}}</b><span></span>
									</li>
									<li><b>{{ trans('pages.usermgnt.tables.birthdate')}}</b><span></span></li>
									
									<li><b>{{ trans('pages.usermgnt.tables.email')}}</b><span></span></li>
								</ul>
							</div>

						</div>

						<div class="inention-div citizen hide">
							<div class="row">
								<div class="col-12 col-sm-3 col-md-3 col-lg-3 col-xl-3" id="report_police"></div>
								<div class="col-12 col-sm-3 col-md-3 col-lg-3 col-xl-3" id="report_fireman_medical"></div>
								<div class="col-12 col-sm-3 col-md-3 col-lg-3 col-xl-3" id="report_city"></div>
								<div class="col-12 col-sm-3 col-md-3 col-lg-3 col-xl-3" id="report_handrail"></div>
								
							</div>
						</div>
							<div class="inention-div operator hide">
							<div class="row">
								<div class="col-12 col-sm-6 col-md-6 col-lg-6 col-xl-6" id="assigned_incidents"></div>
								<div class="col-12 col-sm-6 col-md-6 col-lg-6 col-xl-6" id="completed_incidents"></div>
							</div>
							</div>
			

						<div class="zoom-div mt-4">
							<div class="row">
								<div
									class="col-12 col-sm-6 col-md-6 col-lg-6 col-xl-6 zoom-left">
									<h2>Carte professional</h2>
									<a href="javascript:void(0);" id="business_card1" class="showimage"><i class="fa fa-search-plus showimage" aria-hidden="true"></i></a>
									<a href="javascript:void(0);" id="business_card2" class="showimage"><i class="fa fa-search-plus showimage" aria-hidden="true"></i></a>
								</div>
								<div
									class="col-12 col-sm-6 col-md-6 col-lg-6 col-xl-6 zoom-left">
									<h2>Carte professional</h2>
									<a href="javascript:void(0);" id="id_card1" class="showimage"><i class="fa fa-search-plus showimage" aria-hidden="true"></i></a>
									<a href="javascript:void(0);" id="id_card2" class="showimage"><i class="fa fa-search-plus showimage" aria-hidden="true"></i></a>
								</div>
							</div>
						</div>

						<div
							class="location-div col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 p-0 mt-3">
							<div id="map1" style="width:100%;height:200px;"></div>
						</div>

						<div class="form-group col-12 col-sm-4 col-md-4 col-lg-4 col-xl-5 mt-2 pl-0">
							<label class="w-100">Address</label> <p id="incidents_address"></p>
						</div>

						<div class="form-group col-12 col-sm-4 col-md-4 col-lg-4 col-xl-5 mt-2 pl-0">
							<label class="w-100">Subject Of the Report</label> <p id="subject_report"></p>
						</div>

						<div class="form-group col-12 col-sm-4 col-md-4 col-lg-4 col-xl-5 mt-2 pl-0">
							<label class="w-100">Incident Description</label> <p id="incident_description"></p>
						</div>

						<div class="form-group col-12 col-sm-4 col-md-4 col-lg-4 col-xl-5 mt-2 pl-0">
							<label class="w-100">Other Description</label> <p id="other_description"></p>
						</div>
						<div class="form-group col-12 col-sm-4 col-md-4 col-lg-4 col-xl-5 mt-2 pl-0">
							<label class="w-100">Attachment</label><p id="attachmentinc"></p><p id="attachmentvideoinc"></p>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>

		</div>
	</div>
			<!--end 2janpp-->



		</div>
		<!-- /.row -->
	</div>
	<!-- /.container-fluid -->
</div>
<style>
.user__item_1.disabled{
	display:none;
} 
.chat-header { border: 1px solid #e7e7e7; }
.chat-header i { font-size:15px; }
.chat-wrapper {border: 1px solid #e7e7e7;
    
    background: #fff;
}
.chat-wrapper .chat-inner-section
{
    padding: 5px;    
}
.chat-inner-section .user__item_1 .user__details p.user__name{
border-bottom: 1px dotted grey;  margin-bottom: .1rem; cursor:pointer
}
.chat-header .input-group-addon { border: 1px solid #ced4da; }
.chat-header .input-group-addon i { margin: 5px; border-right: 0px; }
.chat-header input { height:30px; }
.chat-header .col-md-2 { margin-top: 3px; }
</style>
<!-- /.content-header -->
@endsection @section('before-styles') @endsection 

@section('after-scripts') 
<script src="https://maps.googleapis.com/maps/api/js?&libraries=places&key={{ env('GOOGLE_MAP_KEY') }}"></script>

<script src="https://unpkg.com/navigo@4.3.6/lib/navigo.min.js" defer></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/underscore.js/1.8.3/underscore.js" defer></script>
<script src="{{ asset('js/plugins/quickblox/quickblox.min.js') }}" defer></script>
<script src="{{ asset('js/plugins/quickblox/QBconfig.js') }}" defer></script>
<script src="{{ asset('js/plugins/quickblox/user.js') }}" defer></script>
<script src="{{ asset('js/plugins/quickblox/dialog.js') }}" defer></script>
<script src="{{ asset('js/plugins/quickblox/message.js') }}" defer></script>
<script src="{{ asset('js/plugins/quickblox/listeners.js') }}" defer></script>
<script src="{{ asset('js/plugins/quickblox/helpers.js') }}" defer></script>
<script src="{{ asset('js/plugins/quickblox/app.js') }}" defer></script>
<script src="{{ asset('js/plugins/quickblox/login.js') }}" defer></script>
<script src="{{ asset('js/plugins/quickblox/route.js') }}" defer></script>






<script>

var map, marker, circle, marker11;
var bounds = new google.maps.LatLngBounds();

function _get_users()
{
	var login = $('input[type="hidden"][name="hidden_user_id"]').val();
	var userName = $('input[type="hidden"][name="hidden_user_full_name"]').val();
	var userPassword = $('input[type="hidden"][name="hidden_userpassword"]').val();
	var userGroup = $('input[type="hidden"][name="hidden_usergroup"]').val();
	var email = $('input[type="hidden"][name="hidden_email"]').val();
	
	var user = {
        login: login,
        email : email,
        password: userPassword,
        full_name: userName,
        tag_list: userGroup
    };

	localStorage.setItem('user', JSON.stringify(user));

	QB.createSession(function(csErr, csRes) {
		var userRequiredParams = {
                'login':user.login,
                'password': user.password
            };
            if (csErr) {
                loginError(csErr);
            } 
            else 
            {
                var id;
            	QB.login(userRequiredParams, function(loginErr, loginUser){
            		console.log(loginUser); 
            		id = loginUser.id;           		
            	});

            	params = {
            	        tags: user.tag_list,
            	        per_page: 100
            	    };
            	QB.users.get(params, function (err, responce) {
                    var userList = responce.items.map(function(data){
                        return userModule.addToCache(data.user);
                    });

                    var user = JSON.parse(localStorage.getItem('user'));
                    
                    var html = '';
                    $(userList).each(function(k,v){                        
                    	var disabled = ''; if(id == v.id) { console.log(id); console.log(v.id);  disabled=' disabled'; }
                    	html +='<div class="user__item_1'+disabled+'" id="'+v.id+'">';
                    	html +='<span class="user__avatar m-user__img_10" style="display:none;">';
                    	html +='<i class="material-icons m-user_icon">account_circle</i>';
                    	html +='</span>';
                    	html +='<div class="user__details">';
                		html +='<p class="user__name">'+v.name+'</p>';                
            			html +='<p class="user__last_seen" style="display:none;">'+v.last_request_at+'</p>';                
            			html +='</div>';
            			html +='</div>';
                    });
                    
                    html +='<form action="" name="create_dialog" class="dialog_form m-dialog_form_create j-create_dialog_form" style="display:none;">';
            		html +='<input class="dialog_name" name="dialog_name" type="text" autocomplete="off" autocorrect="off" autocapitalize="off" placeholder="Add conversation name" disabled="">';
            		html +='<button class="btn m-create_dialog_btn j-create_dialog_btn" type="submit" name="create_dialog_submit">create</button>';
            		html +='</form>';
            		
                    $('.chat-inner-section').html(html);


                    $(document).on('click', '.user__item_1', function(e){
                    	var $this = $(this);
                    	
                    	$('.user__item_1').each(function(){                        	
                        	$(this).removeClass('selected');
                        })
                        
                    	$(this).addClass('selected');
						console.log($(this).attr('id'));
//                    	$('button[type="submit"][name="create_dialog_submit"]').trigger('click');
                    	
                    	
//                     	userModule.content = document.querySelector('.j-sidebar');
//                         userModule.userListConteiner = userModule.content.querySelector('.j-sidebar__dilog_list');
                    	
                    	
//                     	document.forms.create_dialog.create_dialog_submit.disabled = true;
                        
                        var users = $(this).attr('id'),
                            type = users.length > 2 ? 2 : 3,
                            name = document.forms.create_dialog.dialog_name.value,
                            occupants_ids = [];
                        
                        occupants_ids.push(users);
                        console.log(occupants_ids);
                        if (!name && type === 2) {
                            var userNames = [];
                            
                            _.each(occupants_ids, function (ids) {
                                if (ids === id) {
                                    userNames.push(self.user.name || self.user.login);
                                } else {
                                    userNames.push(userModule._cache[id].name);
                                }
                            });
                            name = userNames.join(', ');
                        }

                        var params = {
                            type: type,
                            occupants_ids: occupants_ids.join(',')
                        };
                        
                        if (type !== 3 && name) {
                            params.name = name;
                        }
                        console.log(params);
                        
                        QB.chat.dialog.create(params, function (err, createdDialog) {
                            if (err) {
                                console.error(err);
                            } else {
                            	console.log(createdDialog._id);
                            	window.location.href = "{{ url('/chat') }}#!/dialog/"+createdDialog._id
//                             	router.navigate('#!/dialog/'+createdDialog._id);
//                            	dialogModule.renderMessages(createdDialog._id);
                            } 	
                        });
                    });
            	});
            }
	});
}

function init() 
{
	map= {
	  	center:new google.maps.LatLng(48.864716, 2.349014),
	  	zoom:5,
	  //disableDoubleClickZoom: true,
	 // draggable:false,
	  //scrollwheel: true,
	  //zoomControl: true
	};

	map = new google.maps.Map(document.getElementById("map"), map);
}

var content = [];
var markers = [];
var circles = [];
var pins = [];

function add_markers(markerArray, lat, lng , type)
{
	clear_markers();
	if(markerArray.length > 0)
	{
		for( i = 0; i < markerArray.length; i++ ) {
		var html = '';	
    	html += '<div class="info_content">';
    	html += '<h3>'+markerArray[i][2]+'</h3>';
    	html += '<p style="margin-top:10px">'+markerArray[i][3]+'</p>';
    	html += '<p>'+markerArray[i][4]+'</p>';
    	html +='</div>';

    	content.push(html);
	}

	
	// Loop through our array of markers & place each one on the map  
    for( i = 0; i < markerArray.length; i++ ) {
        var position = new google.maps.LatLng(markerArray[i][0], markerArray[i][1]);
        bounds.extend(position);
        if(type == "4")
		{
			//marker.setIcon('http://maps.google.com/mapfiles/ms/icons/green-dot.png');
			marker = new google.maps.Marker({
            position: position,
            map:map,
            icon:'http://maps.google.com/mapfiles/ms/icons/green-dot.png',
            title: markerArray[i][2],
            type:markerArray[i][5]    
        	});
		}
		else if(type == "3")
		{
		   //marker.setIcon('http://maps.google.com/mapfiles/ms/icons/yellow-dot.png');
		   marker = new google.maps.Marker({
            position: position,
            map:map,
            icon:'http://maps.google.com/mapfiles/ms/icons/red-dot.png',
            title: markerArray[i][2],
            type:markerArray[i][5]    
        	});	
		}
		else if(markerArray[i][5] == "3")
		{
			marker = new google.maps.Marker({
            position: position,
            map:map,
            icon:'http://maps.google.com/mapfiles/ms/icons/red-dot.png',
            title: markerArray[i][2],
            type:markerArray[i][5]    
        	});
		}
		else if(markerArray[i][5] == "4")
		{
			marker = new google.maps.Marker({
            position: position,
            map:map,
            icon:'http://maps.google.com/mapfiles/ms/icons/green-dot.png',
            title: markerArray[i][2],
            type:markerArray[i][5]    
        	});
		}
		else
		{
			marker = new google.maps.Marker({
            position: position,
            map:map,
            title: markerArray[i][2],
            type:markerArray[i][5]    
        	});
		}
        
        markers.push(marker);
		
        var infoWindow = new google.maps.InfoWindow();
        
     // Allow each marker to have an info window    
        google.maps.event.addListener(marker, 'click', (function(marker, i) {  
            return function() {
                infoWindow.setContent(content[i]);
                infoWindow.open(map, marker);
                if(marker.type=="4")
                {
                 marker.setIcon('http://maps.google.com/mapfiles/ms/icons/green-dot.png');	
                }
                if(marker.type=="3")
                {
                 marker.setIcon('http://maps.google.com/mapfiles/ms/icons/red-dot.png');	
                }
            }
        })(marker, i));
	}
	 }
        // Automatically center the map fitting all markers on the screen
        map.fitBounds(bounds);
       
		if(lat !="" && lng !=""){
            initialLocation = new google.maps.LatLng(lat, lng);
            map.setCenter(initialLocation);
            map.setZoom(12);

         // Add the circle for this city to the map.
           /* circle = new google.maps.Circle({
              strokeColor: '#FF0000',
              strokeOpacity: 0.4,
              strokeWeight: 2,
              fillColor: '#FF0000',
              fillOpacity: 0.35,
              map: map,
              center: new google.maps.LatLng(lat, lng),
              radius: 10000,
              draggable:true
            });*/
		}
   
    
    
}



function setMapOnAll(map) 
{	
	for (var i = 0; i < markers.length; i++) 
	{
  		markers[i].setMap(map);
	}
	
	markers = [];
	
}

function clear_markers()
{
	setMapOnAll(null);
}

function clear_circles()
{
	for(var i in circles)
	{
		circles[i].setMap(null);
	}
	circles = [];
}

function clear_pins()
{
	for(var i in pins)
	{
		pins[i].setMap(null);
	}
	pins = [];
}

var oTable, lat, lng = '';
$(function(){
	init();
	_get_users();

	
    oTable = $('#datatables').DataTable({
      processing: false,
      serverSide: true,
      parseHtml : true,
      ajax: {
          url: '{{ route("backoffice.incidents.list")  }}',	
          type : 'post',
          data: function (d) {
        	  d._token = '{{ csrf_token() }}';
              d.lat = $('input[type="hidden"][name="hidden_lat"]').val();
              d.lng = $('input[type="hidden"][name="hidden_lng"]').val();
              
          },
          dataFilter: function(response){
              // this to see what exactly is being sent back
              var markerArray = [];
              var result = JSON.parse(response);
					
              $(result.data).each(function(k,v){
              	markerArray.push([v.latitude, v.longitude, v.sub_category_name, v.incident_description, v.address,v.ref_user_type_id]);	
              });

              lat = $('input[type="hidden"][name="hidden_lat"]').val();
              lng = $('input[type="hidden"][name="hidden_lng"]').val();
              
              add_markers(markerArray, lat, lng);
              		
              return response;
          },
      },
      
      	columns: [
          { data: 'date', name: 'date'},
          { data: 'address', name: 'address' },
          { data: 'reporter', name: 'reporter' },
          { data: 'first_name', name: 'first_name' },
          { data: 'sub_category_name', name: 'sub_category_name' }, 
          { data: 'incident_description', name: 'incident_description' },
          { data: 'status', name: 'status' },
      ],
      order: [[0, "desc"]]
	});	  
});

/*************************************************************************************************************************/
var input = document.getElementById('search_places');
var autocomplete = new google.maps.places.Autocomplete(input);

google.maps.event.addListener(autocomplete, 'place_changed', function () {
    var place = autocomplete.getPlace();
    lat = place.geometry.location.lat();
    lng = place.geometry.location.lng();

    $('input[type="hidden"][name="hidden_lat"]').val(lat);
    $('input[type="hidden"][name="hidden_lng"]').val(lng);

    //oTable.ajax.reload();   
    $.ajax ({
          url: '{{ route("backoffice.incidents.list")  }}',	
          type : 'post',
          dataType:'json',
          data: {
        	  "_token" : '{{ csrf_token() }}',
              "lat": $('input[type="hidden"][name="hidden_lat"]').val(),
              "lng": $('input[type="hidden"][name="hidden_lng"]').val()
              
          },
          success: function(response){
     
              // this to see what exactly is being sent back
              var markerArray = [];
              var result = response;
              var type = '';

              if(result.recordsTotal > 0)
              {
              	console.log("Test");
              	var type = result.data[0]['ref_user_type_id'];
              $(result.data).each(function(k,v){
              	markerArray.push([v.latitude, v.longitude, v.sub_category_name, v.incident_description, v.address,v.ref_user_type_id]);	
              });

	              	
              }	

              lat = $('input[type="hidden"][name="hidden_lat"]').val();
              lng = $('input[type="hidden"][name="hidden_lng"]').val();
	              
              add_markers(markerArray, lat, lng , type);		  
          },
      });  
});
/*************************************************************************************************************************/

/****DropDown Filter**********/
$('#user_type').change(function(){
var userType = $(this).val();
if(userType.length >0){
    //oTable.ajax.reload();   
    $.ajax ({
          url: '{{ route("backoffice.incidents.citizencops")  }}',	
          type : 'post',
          dataType:'json',
          data: {
        	  "_token" : '{{ csrf_token() }}',
              "lat": $('input[type="hidden"][name="hidden_lat"]').val(),
              "lng": $('input[type="hidden"][name="hidden_lng"]').val(),
              "usertype": userType
              
          },
          success: function(response){
              // this to see what exactly is being sent back
              var markerArray = [];
              var result = response;
					
              $(result.data).each(function(k,v){
              	markerArray.push([v.latitude, v.longitude, v.sub_category_name, v.incident_description, v.address,v.ref_user_type_id]);	
              });

              lat = $('input[type="hidden"][name="hidden_lat"]').val();
              lng = $('input[type="hidden"][name="hidden_lng"]').val();
              
              add_markers(markerArray, lat, lng ,userType);
              		
              return response;
          },
      });  

}
});
/****End Dropdown Filter******/

$(document).on('click', '.view-incident', function(){
	var $this = $(this);
	var incidentId = $this.data('incidentId');

	/* Get data based on incident id */	
});



var count = 0;
var countpointer = 0;
$('#remove-circle').on('click', function(){
	//count =0;
	//circle.setMap(null);
	//clear_circles();
	var lat = $('input[type="hidden"][name="hidden_lat"]').val();
    var lng = $('input[type="hidden"][name="hidden_lng"]').val();
	if(lat !="" && lng !=""){
	countpointer += 1;

    if (countpointer < 100) {    
	marker11 = new google.maps.Marker({
        position: new google.maps.LatLng(lat, lng),
//         icon: 'https://maps.google.com/mapfiles/kml/shapes/parking_lot_maps.png',
        icon: "{{ asset('img/pin.png') }}",
        map: map,
        draggable:true
  	});

  	pins.push(marker11);

  	google.maps.event.addListener(marker11, 'rightclick', (function(marker, i) {
  		alert("Test");
        // return function() {
        // 	 marker.setMap(null);
	       //      countpointer -= 1;
        // }
    })(marker11, i));


  }
}
});


$('#add-circle').on('click', function(){
	var lat = $('input[type="hidden"][name="hidden_lat"]').val();
    var lng = $('input[type="hidden"][name="hidden_lng"]').val();
	//countpointer = 0;
	if(lat !="" && lng !=""){
	count += 1;
    if (count < 100) {
	//marker11.setMap(null);


    circle = new google.maps.Circle({
        strokeColor: '#FF0000',
        strokeOpacity: 0.4,
        strokeWeight: 2,
        fillColor: '#FF0000',
        fillOpacity: 0.35,
        map: map,
        center: new google.maps.LatLng(lat, lng),
        radius: 3000,
        draggable:true,
        clickable : true , editable : true , draggable : true
      });
    circles.push(circle);

	    google.maps.event.addListener(circle, 'rightclick', (function(circle, i) {  
	        return function() {
	            circle.setMap(null);
	            count -= 1;
	        }
	    })(circle, i));
	}
	}
});

//pp 2jan
$(document).on('click','.view-incident',function(e){  
   var incidentId = $(this).attr('data-incident-id');
   $.ajaxSetup({
		headers: {
			'X-CSRF-TOKEN': $('meta[name="csrf-token"]').attr('content')
		}
	});
   $.ajax({
		url: '{{ route("backoffice.incidents.view") }}',
		data: { 'incidentid': incidentId},
		cache:false,
		type: 'POST',   
		dataType: "json",
		beforeSend : function(data)
		{
			$('.loader_a').removeClass('hide');
		},
		success: function (response) {

			var d = response.data;
			console.log(d);
			$('.loader_a').addClass('hide');
			
			$('.modal .modal-dialog .modal-content .modal-body .user-mgm-m7 .row .right-part ul li:eq(0) span').html(d[0]['first_name']+" "+ d[0]['last_name']);
			$('.modal .modal-dialog .modal-content .modal-body .user-mgm-m7 .row .right-part ul li:eq(1) span').html(d[0]['date_of_birth']);
			$('.modal .modal-dialog .modal-content .modal-body .user-mgm-m7 .row .right-part ul li:eq(2) span').html(d[0]['email_id']);

			if(d[0]['profile_image'] !="") $('.modal .modal-dialog .modal-content .modal-body').find('img#profile_image').attr('src', d[0]['profile_image']);

			$('.operator').removeClass('hide');
			$('.citizen ').addClass('hide');
			
			if(d[0]['ref_user_type_id'] != '{{ App\UserType::_TYPE_OPERATOR }}'){
				$('.operator').addClass('hide');
				$('.citizen ').removeClass('hide');	
			if(typeof d[0]['report_police'] !== 'undefined')
			{
				$('#report_police').html("<h2>Report <br>Police<span >"+d[0]['report_police']+"</span></h2>");
			}
			
			if(typeof d[0]['report_fire'] !== 'undefined')
			{
			$('#report_fireman_medical').html("<h2>Report <br> Fireman/medical<span >"+d[0]['report_fire']+"</span></h2>");
			}

			if(typeof d[0]['report_city'] !== 'undefined')
			{
			$('#report_city').html("<h2>Report <br> City<span >"+d[0]['report_city']+"</span></h2>");
			}
			
			if(typeof d[0]['report_handrail'] !== 'undefined')
			{
			$('#report_handrail').html("<h2>Handrail<span >"+d[0]['report_handrail']+"</span></h2>");
			}
		}
		else if(d[0]['ref_user_type_id'] == '{{ App\UserType::_TYPE_OPERATOR }}') {
				$('.operator').removeClass('hide');
				$('.citizen ').addClass('hide');	
			if(typeof d[0]['assigned_incidents'] !== 'undefined')
			{
			$('#assigned_incidents').html("<h2>Assigned<br>incidents<span >"+d[0]['assigned_incidents']+"</span></h2>");
			}

			if(typeof d[0]['completed_incidents'] !== 'undefined')
			{
			$('#completed_incidents').html("<h2>Completed<br>incidents<span >"+d[0]['completed_incidents']+"</span></h2>");
			}
		}
			$('#incidents_address').html(d[0]['address']);
			$('#subject_report').html(d[0]['sub_category_name']);
			$('#incident_description').html(d[0]['incident_description']);
			$('#other_description').html(d[0]['other_description']);
			if(d[0]['photo'] != null)
			{
			var photo= "{{ url('/uploads/incident_image') }}/"+d[0]['photo'];
			$('#attachmentinc').html('<a href="'+photo+'" traget="_blank">View</a>');
			}
			if(d[0]['video'] != null)
			{
			var video= "{{ url('/uploads/incident_video') }}/"+d[0]['video'];
			$('#attachmentvideoinc').html('<a href="'+video+'" traget="_blank">View</a>');
			}

			if(d[0]['business_card1'] !="")$('.modal .modal-dialog .modal-content .modal-body').find('#business_card1').attr('href', d[0]['business_card1']).attr('target','_blank');
			if(d[0]['business_card2'] !="")$('.modal .modal-dialog .modal-content .modal-body').find('#business_card2').attr('href', d[0]['business_card2']).attr('target','_blank');

			if(d[0]['id_card1'] !="")$('.modal .modal-dialog .modal-content .modal-body').find('#id_card1').attr('href', d[0]['id_card1']).attr('target','_blank');
			if(d[0]['id_card2'] !="")$('.modal .modal-dialog .modal-content .modal-body').find('#id_card2').attr('href', d[0]['id_card2']).attr('target','_blank');
			
			$('#myModal').modal('show');
			lat = d[0]['latitude'];
			lng = d[0]['longitude'];
			var position = new google.maps.LatLng(lat, lng);
        	bounds.extend(position);

			map1= {
			  	center:new google.maps.LatLng(lat, lng),
			  	zoom:5,
			  	/*zoomControl: false,
				   zoomControl: false,
				  scaleControl: false,
				  scrollwheel: false,*/
				  //disableDoubleClickZoom: true,
				  //draggable:false
			};

			map1 = new google.maps.Map(document.getElementById("map1"), map1);
			marker = new google.maps.Marker({
            position: position,
            map:map1,
        	});
			
		}
	});
});



</script>

@endsection
