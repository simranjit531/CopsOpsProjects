@extends('backend.layouts.backendapp')
@section('content')
<!-- Content Header (Page header) -->
    <div class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1 class="m-0 text-dark">{{ trans('pages.discussion') }}</h1>
          </div><!-- /.col -->
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="#">Home</a></li>
              <li class="breadcrumb-item active">{{ trans('pages.discussion') }}</li>
            </ol>
          </div><!-- /.col -->
      </div>
  </div>
</div>

  
@endsection
@section('after-scripts')
         
@endsection