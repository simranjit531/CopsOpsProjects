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
                                <input type="text" class="form-control" placeholder="22/02/2018">
                            </div>
                            <div class="col-lg-12">
                                <label class="text-center">et</label>
                                <input type="text" class="form-control" placeholder="22/04/2018">
                            </div>
                            <button type="button" class="actual-btn mt-2 mb-2">Actual</button>
                        </form>
                    </div>
                    
                        <div class="col-12 col-sm-7 col-md-7 col-lg-7 col-xl-7 left-form border-0">
                         <form>
                            <div class="col-lg-12">
                                <label>Nom/Prenom</label>
                                <input type="text" class="form-control" placeholder="">
                            </div>
                            <button type="button" class="actual-btn mt-2">Rechrecher</button>
                        </form>
                    </div>
                        
                        <table class="table table-bordered table-striped">
                            <thead>
                                <tr>
                                    <th>Date</th>
                                    <th>Object</th>
                                    <th>Nom/Prenom</th>
                                </tr>
                            </thead>
                            
                            <tbody>
                                <tr>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                </tr>
                                
                                <tr>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                </tr>
                                
                                <tr>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                </tr><tr>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                </tr>
                            </tbody>
                            
                        </table>
                    
                    </div>    
                        
                </div>
                
                <div class="caree-left-form m9-screen col-12 col-sm-7 col-md-7 col-lg-7 col-xl-7">
                    <h6>Details</h6>
                    
                    <h4><span>12-10-2018</span> <span>Nom delorem</span> <span>Nom de lorem</span></h4>
                    
                    <ul>
                        <li><span>Lorem Ipsum</span> <input type="text" class="form-control"></li>
                        <li><span>Lorem Ipsum</span><input type="text" class="form-control"></li>
                        <li><span>Lorem Ipsum</span> <textarea class="form-control"></textarea></li>
                        <li><span>Lorem Ipsum</span> <textarea class="form-control"></textarea></li>
                        <li><span>Lorem Ipsum</span><input type="text" class="form-control refrence-input"></li>
                    </ul>
                    
                </div>
                
                
                
            </div>
        </div>      
</div>


  
@endsection
@section('after-scripts')
       
@endsection