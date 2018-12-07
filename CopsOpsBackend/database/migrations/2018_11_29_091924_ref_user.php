<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class RefUser extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('ref_user', function (Blueprint $table) {
            $table->increments('id');
            $table->string('user_id',100)->default(null)->unique();
            $table->integer('ref_user_type_id');
			$table->string('user_password',20)->default(null);
			$table->string('first_name',50)->default(null);
			$table->string('last_name',50)->default(null);
			$table->date('date_of_birth')->default(null);
			$table->string('place_of_birth',100)->default(null);
			$table->string('gender',10)->default(null);
            $table->string('phone_number',15);
			$table->string('email_id')->unique();
			$table->string('id_card1',200)->default(null);
			$table->string('id_card2',200)->default(null);
			$table->string('business_card',200)->default(null);
			$table->string('latitude',50)->default(null);
			$table->string('longitude',50)->default(null);
			$table->integer('otp');
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
        Schema::dropIfExists('ref_user');
    }
}
