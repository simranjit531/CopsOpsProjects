<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CopLatLong extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
       Schema::create('cop_lat_long', function (Blueprint $table) {
            $table->increments('id');
            $table->integer('ref_user_id');
			$table->string('latitude',50)->default(null)->change();
			$table->string('longitude',50)->default(null)->change();
            $table->tinyInteger('is_deleted')->default('1');
            $table->timestamps();
			
			/*$table->foreign('ref_user_id')
			->references('id')->on('ref_user')
			->onDelete('cascade');*/
        });
		
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('cop_lat_long');
    }
}
