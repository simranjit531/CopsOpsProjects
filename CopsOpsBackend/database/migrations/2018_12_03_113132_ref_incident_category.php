<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class RefIncidentCategory extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
		Schema::create('ref_incident_category', function (Blueprint $table) {
        $table->increments('id');
		$table->string('category_name',70)->unique();
		$table->string('description')->default(null);
		$table->string('icon')->default(null);
		$table->tinyInteger('is_deleted')->default('1');
		$table->timestamps();
		});
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
       Schema::dropIfExists('ref_incident_category');
    }
}
