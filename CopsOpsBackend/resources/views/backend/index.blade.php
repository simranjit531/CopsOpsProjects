@extends('backend.layouts.backendapp')
@section('content')



<!-- Content Header (Page header) -->
    <div class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1 class="m-0 text-dark">{{ trans('pages.controlCenter') }}</h1>
          </div><!-- /.col -->
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="#">Home</a></li>
              <li class="breadcrumb-item active">{{ trans('pages.controlCenter') }}</li>
            </ol>
          </div>
            
            
            <div class="round-right-part mb-3">
          <!-- SEARCH FORM -->
            <form class="form-inline col-md-8 ml-3 mt-1 float-left">
              <div class="input-group input-group-sm">
                  <div class="input-group-append">
                  <button class="btn btn-navbar" type="submit">
                    <i class="fa fa-search"></i>
                  </button>
                </div>
                <input class="form-control form-control-navbar" type="search" placeholder="Porte" aria-label="Search">
                
              </div>
                
                <div class="col-12 col-sm-2 col-md-2 ml-3 select-box">
                    <select>
                        <option value="" selected>Afficher</option>
                    </select>
                </div>
                
                <div class="col-12 col-sm-4 col-md-4 location-zone ml-3">
                    <ul>
                        <li><a href="">Zone <br>d'internet <i class="fa fa-map-marker" aria-hidden="true"></i></a></li>
                        <li><a href="">Point <br>d'internet <i class="fa fa-arrows-v" aria-hidden="true"></i></a></li>
                    </ul>
                </div>
                
            </form>

            <!-- Right navbar links -->
            <ul class="navbar-nav notification-port">

              <li class="nav-item dropdown">
                <a class="nav-link" data-toggle="dropdown" href="#">
                  <i class="fa fa-comments-o"></i>
                  <span class="badge badge-danger navbar-badge">3</span>
                </a>
                <div class="dropdown-menu dropdown-menu-lg dropdown-menu-right">
                  <a href="#" class="dropdown-item">
                    <!-- Message Start -->
                    <div class="media">
                      <img src="{{asset('img/user1-128x128.jpg')}}" alt="User Avatar" class="img-size-50 mr-3 img-circle">
                      <div class="media-body">
                        <h3 class="dropdown-item-title">
                          Brad Diesel
                          <span class="float-right text-sm text-danger"><i class="fa fa-star"></i></span>
                        </h3>
                        <p class="text-sm">Call me whenever you can...</p>
                        <p class="text-sm text-muted"><i class="fa fa-clock-o mr-1"></i> 4 Hours Ago</p>
                      </div>
                    </div>
                    <!-- Message End -->
                  </a>
                  <div class="dropdown-divider"></div>
                  <a href="#" class="dropdown-item">
                    <!-- Message Start -->
                    <div class="media">
                      <img src="{{asset('img/user8-128x128.jpg')}}" alt="User Avatar" class="img-size-50 img-circle mr-3">
                      <div class="media-body">
                        <h3 class="dropdown-item-title">
                          John Pierce
                          <span class="float-right text-sm text-muted"><i class="fa fa-star"></i></span>
                        </h3>
                        <p class="text-sm">I got your message bro</p>
                        <p class="text-sm text-muted"><i class="fa fa-clock-o mr-1"></i> 4 Hours Ago</p>
                      </div>
                    </div>
                    <!-- Message End -->
                  </a>
                  <div class="dropdown-divider"></div>
                  <a href="#" class="dropdown-item">
                    <!-- Message Start -->
                    <div class="media">
                      <img src="{{asset('img/user3-128x128.jpg')}}" alt="User Avatar" class="img-size-50 img-circle mr-3">
                      <div class="media-body">
                        <h3 class="dropdown-item-title">
                          Nora Silvester
                          <span class="float-right text-sm text-warning"><i class="fa fa-star"></i></span>
                        </h3>
                        <p class="text-sm">The subject goes here</p>
                        <p class="text-sm text-muted"><i class="fa fa-clock-o mr-1"></i> 4 Hours Ago</p>
                      </div>
                    </div>
                    <!-- Message End -->
                  </a>
                  <div class="dropdown-divider"></div>
                  <a href="#" class="dropdown-item dropdown-footer">See All Messages</a>
                </div>
              </li>
                
              <li class="nav-item dropdown">
                <a class="nav-link" data-toggle="dropdown" href="#">
                  <i class="fa fa-bell-o"></i>
                  <span class="badge badge-warning navbar-badge">15</span>
                </a>
                <div class="dropdown-menu dropdown-menu-lg dropdown-menu-right">
                  <span class="dropdown-item dropdown-header">15 Notifications</span>
                  <div class="dropdown-divider"></div>
                  <a href="#" class="dropdown-item">
                    <i class="fa fa-envelope mr-2"></i> 4 new messages
                    <span class="float-right text-muted text-sm">3 mins</span>
                  </a>
                  <div class="dropdown-divider"></div>
                  <a href="#" class="dropdown-item">
                    <i class="fa fa-users mr-2"></i> 8 friend requests
                    <span class="float-right text-muted text-sm">12 hours</span>
                  </a>
                  <div class="dropdown-divider"></div>
                  <a href="#" class="dropdown-item">
                    <i class="fa fa-file mr-2"></i> 3 new reports
                    <span class="float-right text-muted text-sm">2 days</span>
                  </a>
                  <div class="dropdown-divider"></div>
                  <a href="#" class="dropdown-item dropdown-footer">See All Notifications</a>
                </div>
              </li>
                
              
            </ul>
      </div>
            
            <!-- /.col -->
            
            
            <div class="col-sm-12 dashboard-map mb-1">
                <iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d6239.374350666934!2d2.2989443942268655!3d48.889976060830215!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x47e66f98b0100d49%3A0x432b4ce2e4581d5d!2sAvenue+de+la+Porte+d&#39;Asni%C3%A8res%2C+France!5e0!3m2!1sen!2sin!4v1543925728985" width="100%" height="450" frameborder="0" style="border:0" allowfullscreen></iframe>
            </div>
            
            
            <div class="col-sm-12 journal-table mt-2">
                <div class="row">
                    <div class="table-format col-sm-9">
                        <h2>Journal signalments &amp; intervenants <i class="fa fa-map-marker" aria-hidden="true"></i></h2>
                        <table class="table table-bordered table-striped">
                            <thead>
                                <tr>
                                    <th>Date</th>
                                    <th>Address</th>
                                    <th>Text</th>
                                    <th>Text</th>
                                    <th>Text</th>
                                    <th>Description</th>
                                    <th>Text</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>14/11/18</td>
                                    <td>Lorem</td>
                                    <td>Lorem</td>
                                    <td>Lorem</td>
                                    <td>Lorem</td>
                                    <td>Lorem Ipsum is simply dummy text of the printing and typesetting industry. </td>
                                    <td>lorem</td>
                                </tr>

                                <tr>
                                    <td>14/11/18</td>
                                    <td>Lorem</td>
                                    <td>Lorem</td>
                                    <td>Lorem</td>
                                    <td>Lorem</td>
                                    <td>Lorem Ipsum is simply dummy text of the printing and typesetting industry. </td>
                                    <td>lorem</td>
                                </tr>

                                <tr>
                                    <td>14/11/18</td>
                                    <td>Lorem</td>
                                    <td>Lorem</td>
                                    <td>Lorem</td>
                                    <td>Lorem</td>
                                    <td>Lorem Ipsum is simply dummy text of the printing and typesetting industry. </td>
                                    <td>lorem</td>
                                </tr>

                                <tr>
                                    <td>14/11/18</td>
                                    <td>Lorem</td>
                                    <td>Lorem</td>
                                    <td>Lorem</td>
                                    <td>Lorem</td>
                                    <td>Lorem Ipsum is simply dummy text of the printing and typesetting industry. </td>
                                    <td>lorem</td>
                                </tr>

                                <tr>
                                    <td>14/11/18</td>
                                    <td>Lorem</td>
                                    <td>Lorem</td>
                                    <td>Lorem</td>
                                    <td>Lorem</td>
                                    <td>Lorem Ipsum is simply dummy text of the printing and typesetting industry. </td>
                                    <td>lorem</td>
                                </tr>

                                <tr>
                                    <td>14/11/18</td>
                                    <td>Lorem</td>
                                    <td>Lorem</td>
                                    <td>Lorem</td>
                                    <td>Lorem</td>
                                    <td>Lorem Ipsum is simply dummy text of the printing and typesetting industry. </td>
                                    <td>lorem</td>
                                </tr>

                                <tr>
                                    <td>14/11/18</td>
                                    <td>Lorem</td>
                                    <td>Lorem</td>
                                    <td>Lorem</td>
                                    <td>Lorem</td>
                                    <td>Lorem Ipsum is simply dummy text of the printing and typesetting industry. </td>
                                    <td>lorem</td>
                                </tr>

                                <tr>
                                    <td>14/11/18</td>
                                    <td>Lorem</td>
                                    <td>Lorem</td>
                                    <td>Lorem</td>
                                    <td>Lorem</td>
                                    <td>Lorem Ipsum is simply dummy text of the printing and typesetting industry. </td>
                                    <td>lorem</td>
                                </tr>

                                


                            </tbody>
    </table>
                    </div>
                    <div class="col-sm-3 mini-selection float-right">
                        <h2>Chat en Direct</h2>
                        <div class="inner-section"></div>
                    </div>
                </div>
            </div>
            
            
            
        </div><!-- /.row -->
      </div><!-- /.container-fluid -->
    </div>
    <!-- /.content-header -->
@endsection
@section('before-styles')

@endsection