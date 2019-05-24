<?php

use Illuminate\Database\Seeder;

class UsersTableSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    { 
		DB::table('ref_user')->insert([
            'user_id' => uniqid(),
            'ref_user_type_id' => '5',
            'email_id' => 'backofficeuser1@mail.com',
            'user_password' => bcrypt('password'),
            'first_name' =>'Backoffice',
            'last_name' =>'User 1',

        ]);
    }
}
