@extends('backend.layouts.backendapp')
@section('content')
<div class="tab-div">
	<ul>
		<li><a href="{{ url('/archivecenter')}}" >{{ trans('pages.archive.reportlog')}}</a></li>
		<li><a href="{{ url('/currenthand')}}" class="active">{{ trans('pages.archive.currenthand')}}</a></li>
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
          <li class="breadcrumb-item"><a href="#">Home</a></li>
          <li class="breadcrumb-item active">{{ trans('pages.archiveCenter') }}</li>
      </ol>
  </div><!-- /.col -->
</div>
</div>
</div>
<div class='archivecenter'>
    <div class="caree-form col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 border main-archive">
        <div class="row">
            <div class="archive-center-16 col-12 col-sm-5 col-md-5 col-lg-5 col-xl-5 border-right">
                <h2>Cree Une Equipe</h2>

                <div class="row">

                    <div class="col-12 col-sm-5 col-md-5 col-lg-5 col-xl-5 left-form p-0">
                        <form>
                            <div class="col-lg-12">
                                <label>Date</label>
                                <input type="text" class="form-control" name="fromdate"  id="fromdate" readonly="readonly">
                            </div>
                            <div class="col-lg-12">
                                <label class="text-center">et</label>
                                <input type="text" class="form-control" name="todate" id="todate" readonly="readonly">
                            </div>
                            <button type="button" id="search-form" class="actual-btn mt-2 mb-2">Actual</button>
                        </form>
                    </div>
                    
                    <div class="col-12 col-sm-7 col-md-7 col-lg-7 col-xl-7 left-form border-0">
                       <form>
                        <div class="col-lg-12">
                            <label>{{ trans('pages.usermgnt.tables.firstname') }} / {{ trans('pages.usermgnt.tables.lastname') }}</label>
                            <input class="form-control form-control-navbar" type="text" name="name" id="name" placeholder="{{trans('pages.usermgnt.enternamecitizen')}}" aria-label="Search">
                        </div>
                        <!--<button type="button" class="actual-btn mt-2">Rechrecher</button>-->
                    </form>
                </div>
                <table class="table table-bordered table-striped" id="archiveCurrentHand">
                    <thead>
                        <tr>
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

        <div class="caree-left-form m9-screen col-12 col-sm-7 col-md-7 col-lg-7 col-xl-7 rightPart">
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
        <li><span>Address</span> <input type="text" value="" class="form-control addressinc" readonly="readonly"></li>
        <li><span>{{ trans('pages.usermgnt.object') }}</span><input type="text" value="" class="form-control objectinc" readonly="readonly"></li>
        <li><span>Description</span> <textarea class="form-control descriptioninc" readonly="readonly"></textarea></li>
        <li><span>Attachement</span> <p id="attachmentinc"></p><p id="attachmentvideoinc"></p></li>
        <li><span>Reference</span><input type="text" value="" class="form-control refrence-input refrence-input-info" readonly="readonly"></li>
    </ul>
</div>

<div class="caree-left-form m9-screen col-12 col-sm-7 col-md-7 col-lg-7 col-xl-7 rightReportPart">
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
    <li><span>Address</span> <input type="text" value="" class="form-control addressinc" readonly="readonly"></li>
    <li><span>{{ trans('pages.usermgnt.object') }}</span><input type="text" value="" class="form-control objectinc" readonly="readonly"></li>
    <li><span>Description</span> <textarea class="form-control commentreport" readonly="readonly"></textarea></li>
    <li><span>Reference</span><input type="text" value="" id="referencereport" class="form-control refrence-input refrence-input-report" readonly="readonly"></li>
</ul>
</div>



</div>
</div>      
</div>


  
@endsection
@section('after-scripts')
<script>
    $(function() {

        $("#fromdate").datepicker();
        $("#todate").datepicker();
        var oTable =$('#archiveCurrentHand').DataTable({
            processing: true,
            serverSide: true,
            //ajax: '{{ url("/currenthanddata")  }}',
            ajax: {
              url: '{{ url("/currenthanddata")  }}',
              data: function (d) {
                  d.first_name = $('input[name=name]').val();
                  d.fromdate = $('input[name=fromdate]').val();
                  d.todate = $('input[name=todate]').val();
              }
          },
          columns: [
          { data: 'firstlast', name: 'firstlast' },
          { data: 'sub_category_name', name: 'sub_category_name' },
          { data: 'address', name: 'address' },
          { data: 'statuss', name: 'statuss' },
          { data: 'view', name : 'view', orderable: false, searchable: false},
          ],
          order: [[0, "asc"]],
      });

        $('#name').on('keyup', function(e) {
            oTable.draw();   
         });

        $('#search-form').on('submit', function(e) {
            oTable.draw();
        });

        $('.rightPart').hide();
        $('.rightReportPart').hide();

        $(document).on('click','.reportArchiveInc',function(e)
        {
          $('.rightPart').hide();
          $('.rightReportPart').show();
      })

        $(document).on('click','.informationArchiveInc',function(e)
        {
          $('.rightPart').show();
          $('.rightReportPart').hide();
      })

        $(document).on('click','#viewhandrail',function(e){   
           var incidentid = $(this).attr('rel');
           $.ajaxSetup({
              headers: {
                'X-CSRF-TOKEN': $('meta[name="csrf-token"]').attr('content')
            }
        });
           $.ajax({
              url: "{{ url('/viewhandrail') }}",
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
				if(d.data[0]['photo'] != null)
				{
				var photo= "{{ url('/uploads/incident_image') }}/"+d.data[0]['photo'];
				$('#attachmentinc').html('<a href="'+photo+'" traget="_blank">View</a>');
				}
				if(d.data[0]['video'] != null)
				{
				var video= "{{ url('/uploads/incident_video') }}/"+d.data[0]['video'];
				$('#attachmentvideoinc').html('<a href="'+video+'" traget="_blank">View</a>');
				}

                $('.commentreport').val(d.data[0]['comment']);
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