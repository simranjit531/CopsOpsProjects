@extends('backend.layouts.backendapp') @section('content')
<!-- Content Header (Page header) -->

<style>
video {
	height:300px !important;
}
.modal-content {
    box-shadow: none !important;
	background-color: transparent !important;
    border: none !important;
}
.modal-dialog {    
    margin: 6.75rem auto !important;
}	
.chat-text-sec a img{ height : 100px; }
</style>
<script>BASE_URL = "{{ asset('') }}"</script>
<div class="content-header">
	<div class="container-fluid">
		<div class="row mb-2">
			<div class="col-sm-6">
				<h1 class="m-0 text-dark">{{ trans('pages.discussion') }}</h1>
			</div>
			<!-- /.col -->
			<div class="col-sm-6">
				<ol class="breadcrumb float-sm-right">
					<li class="breadcrumb-item"><a href="#">Home</a></li>
					<li class="breadcrumb-item active">{{ trans('pages.discussion') }}</li>

					<input type="hidden" name="hidden_id"
						value="{{ Auth::user()->id }}" />
					<input type="hidden" name="hidden_user_id"
						value="{{ Auth::user()->user_id }}" />
					<input type="hidden" name="hidden_user_full_name"
						value="{{ Auth::user()->first_name.' '.Auth::user()->last_name }}" />
					<input type="hidden" name="hidden_usergroup" value="copops" />
					<input type="hidden" name="hidden_email"
						value="{{ Auth::user()->email_id }}" />
				</ol>
			</div>
			<!-- /.col -->
		</div>
	</div>

	<div class="container-fluid">
		<div class="chating-wraper">
			<div class="col-12 col-sm-12 col-md-12 col-lg-12">
				<div class="row">
					<div class="col-12 col-sm-4 col-md-4 col-lg-4 chat-userlist">
						<div class="chat-userlist-inner">
							<nav class="chat-nav">
								<div class="nav nav-tabs" id="nav-tab" role="tablist">
									<a class="nav-item nav-link " data-type="userlist" data-toggle="tab"
										href="#chat-user-list" role="tab"> <img
										src="images/clipboard.png" alt="user" /></a>
									<div class="search-sec">
										<!-- <p class="text-center">
											<span class="userlist"><i class="fa fa-circle" aria-hidden="true"></i></span>
											<span class="recentchats"><i class="fa fa-dot-circle-o" aria-hidden="true"></i></span>
										</p> -->
										<input type="text" name="_search_term" placeholder="{{ trans('pages.research') }}..">
									</div>
									<a class="nav-item nav-link active" data-type="recentchats" data-toggle="tab"
										href="#chat-list" role="tab"> <img src="images/chat.png"
										alt="chat" />
									</a>
								</div>
							</nav>
						</div>
						<div class="tab-content" id="nav-tabContent">
							<div class="tab-pane fade" id="chat-user-list" role="tabpanel">
								<ul class="users-listing"></ul>
							</div>
							<div class="tab-pane active" id="chat-list" role="tabpanel">
								<ul class="recent-listing"></ul>
							</div>
						</div>
					</div>
					<div class="col-12 col-sm-8 col-md-8 col-lg-8 p-2 chat-wraper">
						<div class="chat-heading mb-3">{{ Auth::user()->first_name.' '.Auth::user()->last_name }}</div>
						<div style="display:inline-block;width:100%;"></div>
						<div class="chatBox"></div>
						<div class="write-msg">
							<img class="upload" id="upload_document" src="images/upload-arrow.png" alt="upload-arrow" />
							<input type="file" name="upload_document" style="display: none;" disabled data-attr="{{ route('ajax.upload.document') }}"/> 
							<input type="text" class="client_chat" name="_text_message" placeholder="{{ trans('pages.writemessage') }}" disabled/>
							<input type="hidden" name="_receiver_id"/>
							<input type="hidden" name="_receiver_name"/>
							<input type="hidden" name="page_length" value="0"/>
							<input type="hidden" name="frenchme" value="{{ trans('pages.frenchme') }}"/>							
							@csrf()
							<div class="sublit-chat"><input class="btn-send chat_btn" type="submit" value="." /></div>
							
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>


<div class="loading">Loading&#8230;</div>
<div class="loading1">Loading&#8230;</div>
@endsection @section('after-scripts')

<!-- Lightbox -->
<link href="{{ asset('js/plugins/lightbox2/src/css/lightbox.css') }}" rel="stylesheet">
<script src="{{ asset('js/plugins/lightbox2/src/js/lightbox.js') }}"></script>

<link href="https://vjs.zencdn.net/7.4.1/video-js.css" rel="stylesheet">
 
<!-- If you'd like to support IE8 (for Video.js versions prior to v7) -->
<script src="https://vjs.zencdn.net/ie8/1.1.2/videojs-ie8.min.js"></script>

<script src='https://vjs.zencdn.net/7.4.1/video.js'></script>

<link href="{{ asset('css/videopopup.css') }}" rel="stylesheet">
<script src="{{ asset('js/videopopup.js') }}"></script>

<script>
$('.nav-item').on("click", function(){
	let $this = $(this);
	
	let className = $this.attr('data-type');
	$('span.userlist i').toggleClass('fa-dot-circle-o fa-circle');
	$('span.recentchats i').toggleClass('fa-dot-circle-o fa-circle');

});

</script>
	@endsection