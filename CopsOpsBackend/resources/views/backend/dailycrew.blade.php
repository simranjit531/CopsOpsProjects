@extends('backend.layouts.backendapp')
@section('content')
<div class="tab-div">
	<ul>
		<li><a href="{{ url('/usermanagement')}}">{{ trans('pages.usermgnt.listofusers')}}</a></li>
		<li><a href="{{ url('/dailycrew')}}" class="active">{{ trans('pages.usermgnt.dailycrew')}}</a></li>
		<li><a href="{{ url('/validationofregistrants')}}">{{ trans('pages.usermgnt.validationofregistrants')}}</a></li>
		<li><a href="{{ url('/accountrefuses')}}">{{ trans('pages.usermgnt.accountrefuses')}}</a></li>
	</ul>
</div>
<!-- Content Header (Page header) -->
<div class="content-header">
	<div class="container-fluid">
		<div class="row mb-2">
			<div class="col-sm-6">
				<h1 class="m-0 text-dark">{{ trans('pages.userManagement') }}</h1>
			</div><!-- /.col -->
			<div class="col-sm-6">
				<ol class="breadcrumb float-sm-right">
					<li class="breadcrumb-item"><a href="#">Home</a></li>
					<li class="breadcrumb-item active">{{ trans('pages.userManagement') }}</li>
				</ol>
			</div><!-- /.col -->
		</div>
	</div>
</div>
<div class="user-management">
	 <div class="caree-form col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12">
            <div class="row">
                <div class="caree-left-form col-12 col-sm-5 col-md-5 col-lg-5 col-xl-5">
                    <h2>Cree Une Equipe</h2>
                    <form>
                        <div class="col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 mb-3">
                            <label>Lorem ipsum</label>
                            <input type="text" class="form-control">
                        </div>
                        <div class="col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 mb-3">
                            <label>Lorem ipsum</label>
                            <input type="text" class="form-control">
                        </div>
                        <div class="col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 mb-3">
                            <label>Lorem ipsum</label>
                            <input type="text" class="form-control">
                        </div>
                        <div class="col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 mb-3">
                            <label>Lorem ipsum</label>
                            <input type="text" class="form-control">
                        </div> 
                        <a href="" class="new-add mt-2"><i class="fa fa-plus-circle" aria-hidden="true"></i> Add New</a>
                        <div class="clearfix"></div>
                        <button type="submit" class="submit-btn">Submit</button>
                    </form>
                </div>
                
                <div class="caree-left-form col-12 col-sm-7 col-md-7 col-lg-7 col-xl-7">
                    <ul class="list-heading">
                        <li>Nom de</li>
                        <li>Effectif</li>
                    </ul>
                    <h3>Lundi 28-12-2018</h3>
                    <ul class=inner-listing>
                        <li><a href="">Lorem ipsum <span>12</span></a></li>
                        <li><a href="">Lorem ipsum <span>12</span></a></li>
                        <li><a href="">Lorem ipsum <span>12</span></a></li>
                    </ul>
                    <h3>Lundi 28-12-2018</h3>
                    <ul class=inner-listing>
                        <li><a href="">Lorem ipsum <span>12</span></a></li>
                        <li><a href="">Lorem ipsum <span>12</span></a></li>
                        <li><a href="">Lorem ipsum <span>12</span></a></li>
                        <li><a href="">Lorem ipsum <span>12</span></a></li>
                        <li><a href="">Lorem ipsum <span>12</span></a></li>
                        <li><a href="">Lorem ipsum <span>12</span></a></li>
                    </ul>
                    
                    <h3>Lundi 28-12-2018</h3>
                    <ul class=inner-listing>
                        <li><a href="">Lorem ipsum <span>12</span></a></li>
                        <li><a href="">Lorem ipsum <span>12</span></a></li>
                        <li><a href="">Lorem ipsum <span>12</span></a></li>
                    </ul>
                </div>
                
                
                
            </div>
        </div>
</div>
@endsection
@section('before-styles')

@endsection
@section('after-scripts')
<script>
	$(function() {
	});
</script>          
@endsection