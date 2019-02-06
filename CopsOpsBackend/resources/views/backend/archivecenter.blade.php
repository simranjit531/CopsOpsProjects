@extends('backend.layouts.backendapp')
@section('content')
<div class="tab-div">
	<ul>
		<li><a href="{{ url('/archivecenter')}}" class="active">{{ trans('pages.archive.reportlog')}}</a></li>
		<li><a href="{{ url('/currenthand')}}" >{{ trans('pages.archive.currenthand')}}</a></li>
	</ul>
</div>
<!-- Content Header (Page header) -->
    <div class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1 class="m-0 text-dark">{{ trans('pages.archiveCenter') }}</h1>
          </div><!-- /.col -->
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="#">{{ trans('pages.home')
						}}</a></li>
              <li class="breadcrumb-item active">{{ trans('pages.archiveCenter') }}</li>
            </ol>
          </div><!-- /.col -->
      </div>
  </div>
</div>
<div class='archivecenter'>
        <div class="caree-form col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 main-archive">
            <div class="row">
                <div class="caree-left-form archive-center-left col-12 col-sm-6 col-md-6 col-lg-6 col-xl-6">
                    <h2>{{ trans('pages.archive.INTERVENTIONSATTRIBUEES')}}</h2>
                    <div class="row">

                    <div class="col-12 col-sm-6 col-md-6 col-lg-6 col-xl-5 left-form p-0">
                        <form>
                            <div class="col-lg-12">
                                <label>{{ trans('pages.fromdate')}}</label>
                                <input type="text" class="form-control" name="fromdate"  id="fromdate" readonly="readonly">
                            </div>
                            <div class="col-lg-12">
                                <label class="text-center">{{ trans('pages.todate')}}</label>
                                <input type="text" class="form-control" name="todate" id="todate" readonly="readonly">
                            </div>
                           
                            <button type="button" id="search-form" class="actual-btn mt-2 mb-2">{{ trans('pages.Actual')}}</button>
                        </form>
                    </div>
                    
                    <div class="col-12 col-sm-7 col-md-7 col-lg-7 col-xl-7 left-form border-0">
                           <form>
                            <div class="col-lg-12">
                                <label>{{ trans('pages.usermgnt.tables.firstname') }} / {{ trans('pages.usermgnt.tables.lastname') }}</label>
                                <input class="form-control form-control-navbar" type="text" name="name" id="name" placeholder="{{trans('pages.usermgnt.enternamecitizen')}}" aria-label="Search">
                            </div>
                            <!-- 
                             <div class="bootstrap-timepicker col-lg-12">
                                        <label class="text-center">et</label>
                                          <input type="text" name="totime" id="totime" class="form-control">
                             </div>
                             -->
                            <!--<button type="button" class="actual-btn mt-2">Rechrecher</button>-->
                        </form>
                    </div>
                    </div>
					<div class="table-responsive">
                     <table class="table table-bordered table-striped" id="archivedata">
                    <thead>
                        <tr>
                            <th>{{ trans('pages.datatime') }}</th>
                            <th>{{ trans('pages.usermgnt.tables.firstname') }} / {{ trans('pages.usermgnt.tables.lastname') }}</th>  							
  							<th>{{ trans('pages.usermgnt.object') }}</th>
  							<th>{{ trans('pages.archive.interventionAddress') }}</th>
  							<th>{{ trans('pages.archive.status')}}</th>
  							
  							<th></th>
                        </tr>
                    </thead>
                    
					         </table>
					</div>
                 
              
                </div>
                
                <div class="caree-left-form m9-screen col-12 col-sm-6 col-md-6 col-lg-6 col-xl-6 rightPart">
                  <div class="inner-tab">
                     <div class="tab-div">
                          <ul>
                            <li><a href="javascript:void(0)" class="active">{{ trans('pages.archive.generalInformation')}}</a></li>
                            <li><a href="javascript:void(0)" class="reportArchiveInc">{{ trans('pages.archive.report')}}</a></li>
                          </ul>
                        </div>
                      </div>
                    
                    <h4><span class="float-right" id="dateinc"></span> <span class="text-center" id="objectinc"></span> <span class="float-right" id="firstlastinc"></span> </h4>
                    
                    <ul>
                        <li><span>{{ trans('pages.usermgnt.address')}}</span> <input type="text" value="" class="form-control addressinc" readonly="readonly"></li>
                        <li><span>{{ trans('pages.usermgnt.object') }}</span><input type="text" value="" class="form-control objectinc" readonly="readonly"></li>
                        <li><span>Description</span> <textarea class="form-control descriptioninc" readonly="readonly"></textarea></li>
                        <li><span>{{ trans('pages.Reference') }}</span><input type="text" value="" class="form-control refrence-input refrence-input-info" readonly="readonly"></li>
                         <li><span>{{ trans('pages.Attachement') }}</span> <p id="attachmentinc"></p><p id="attachmentvideoinc"></p></li>
<!--                          <li><span>Signature</span> <p id="signature"></p></li> -->
                        
<!--                         <li><span>Signature</span><div id="reportsignature"></div></li> -->
                    </ul>
                </div>

                <div class="caree-left-form m9-screen col-12 col-sm-6 col-md-6 col-lg-6 col-xl-6 rightReportPart">
                  <div class="inner-tab">
                     <div class="tab-div">
                          <ul>
                            <li><a href="javascript:void(0)" class="informationArchiveInc">{{ trans('pages.archive.generalInformation')}}</a></li>
                            <li><a href="javascript:void(0)" class="active">{{ trans('pages.archive.report')}}</a></li>
                          </ul>
                        </div>
                      </div>
                    
                    <h4><span class="float-right" id="dateincreport"></span> <span class="text-center" id="objectincreport"></span> <span class="float-right" id="firstlastincreport"></span> </h4>
                    
                    <ul>
                        <li><span>{{ trans('pages.usermgnt.address')}}</span> <input type="text" value="" class="form-control addressinc" readonly="readonly"></li>
                        <li><span>{{ trans('pages.usermgnt.object') }}</span><input type="text" value="" class="form-control objectinc" readonly="readonly"></li>
                        <li><span>Description</span> <textarea class="form-control commentreport" readonly="readonly"></textarea></li>
                        <li><span>{{ trans('pages.Reference') }}</span><input type="text" value="" id="referencereport" class="form-control refrence-input refrence-input-report" readonly="readonly"></li>
                        <li><span>Signature</span><div id="reportsignature_1"></div></li>
                    </ul>
                </div>
                
                
                
            </div>
        </div>
    
</div>


  
@endsection
@section('after-scripts')

<!-- Lightbox -->
<link href="{{ asset('js/plugins/lightbox2/src/css/lightbox.css') }}" rel="stylesheet">
<script src="{{ asset('js/plugins/lightbox2/src/js/lightbox.js') }}"></script>
      
	  <script>
	var oTable;
	$(function() {

		$("#fromdate").datepicker().on('changeDate', function (ev) {		   
			$("#todate").val($(this).val());
			$(this).datepicker('hide');
		});
		
        $("#todate").datepicker().on('changeDate', function (ev) {
		   $(this).datepicker('hide');;
        });


		
		oTable = $('#archivedata').DataTable({
            processing: true,
            serverSide: true,
//             ajax: '{{ url("/archivedata")  }}',
			ajax: {
              url: '{{ url("/archivedata")  }}',
              data: function (d) {
                  d.first_name = $('input[name=name]').val();
                  d.fromdate = $('input[name=fromdate]').val();
                  d.todate = $('input[name=todate]').val();
              }
          	},
            columns: [
        	{ data: 'date', name: 'date' },
            { data: 'firstlast', name: 'firstlast' },
            { data: 'sub_category_name', name: 'sub_category_name'},
            { data: 'address', name: 'address' },
            { data: 'statuss', name: 'statuss' },
            { data: 'view', name : 'view', orderable: false, searchable: false},
        ],
        order: [[0, "desc"]],
        });

    $('.rightPart').hide();
     $('.rightReportPart').hide();

    $(document).on('click','.reportArchiveInc',function(e)
    {
      $('.rightPart').hide();
      $('.rightReportPart').show();
    })
	
	$('#name').on('keyup', function(e) {
            oTable.draw();   
         });

    $(document).on('click','.informationArchiveInc',function(e)
    {
      $('.rightPart').show();
      $('.rightReportPart').hide();
    })
    
     $('#search-form').on('click', function(e) {
            oTable.draw();
        });

    $(document).on('click','#viewIncident',function(e){   
     var incidentid = $(this).attr('rel');
     $.ajaxSetup({
      headers: {
        'X-CSRF-TOKEN': $('meta[name="csrf-token"]').attr('content')
      }
    });
     $.ajax({
      url: "{{ url('/viewincident') }}",
      data: { 'incidentid': incidentid},
      cache:false,
      type: 'POST',   
      dataType: "json",
      beforeSend : function(data)
      {
        $('.loader_a').removeClass('hide');
      },
      success: function (d) {
        console.log(d);
        $('.loader_a').addClass('hide');
        $('.rightPart').show();
        $('#dateinc').html(d.data[0]['updated_on']);
        $('#objectinc').html(d.data[0]['sub_category_name']);
        $('#firstlastinc').html(d.data[0]['first_name']+' '+d.data[0]['last_name']);
        $('#dateincreport').html(d.data[0]['closedate']);
        $('#objectincreport').html(d.data[0]['sub_category_name']);
        $('#firstlastincreport').html(d.data[0]['cop_first_name']+' '+d.data[0]['cop_last_name']);

        $('.commentreport').val(d.data[0]['comment']);
		if(d.data[0]['photo'] != null)
		{
		var photo= "{{ url('/uploads/incident_image') }}/"+d.data[0]['photo'];
// 			$('#attachmentinc').html('<a href="'+photo+'" traget="_blank">View</a>');
// 			$('#attachmentinc').html('<img src="'+photo+'" style="width:200px;">');

			IMG = $('<a href="'+photo+'" data-lightbox="image-1">')		    	
	    	.append('<img src="'+photo+'" style="width:200px;"></a>')			
		
			$('#attachmentinc').html(IMG);

			//lightbox open:
			IMG.click(function(){
			    lightbox.start($(this));
			    return false;
			})

			
		}
		if(d.data[0]['video'] != null)
		{
		var video= "{{ url('/uploads/incident_video') }}/"+d.data[0]['video'];
		$('#attachmentvideoinc').html('<a href="'+video+'" traget="_blank">View</a>');
		}
    if(d.data[0]['signature'] != null)
    {
    var signature= "{{ url('/uploads/signature') }}/"+d.data[0]['signature'];
//     	$('#signature').html('<a href="'+signature+'" traget="_blank">View</a>');
//     	$('#reportsignature').html('<img src="'+signature+'" style="width:200px;">');

    	IMG1 = $('<a href="'+signature+'" data-lightbox="image-1">')		    	
    	.append('<img src="'+signature+'" style="width:200px;"></a>')			
	
		$('#reportsignature').html(IMG1);

    	IMG2 = $('<a href="'+signature+'" data-lightbox="image-1">')		    	
    	.append('<img src="'+signature+'" style="width:200px;"></a>')	
    	
    	$('#reportsignature_1').html(IMG2);

		//lightbox open:
		IMG1.click(function(){
		    lightbox.start($(this));
		    return false;
		})
    }
      
        
        $('.addressinc').val(d.data[0]['address']);
        $('.objectinc').val(d.data[0]['sub_category_name']);
        $('.descriptioninc').val(d.data[0]['incident_description']);
        $('.refrence-input-info').val(d.data[0]['reference']);
		$('#referencereport').val(d.data[0]['closereference']);
      }
    });

     });
	});
</script>   
	  
@endsection