<?php
$type=$_POST['type'];
$subdir=false;

$addpart='';
if($type==1) {
	$subdir="phone";
	$addpart='1_';
}
elseif($type==2) {
	$subdir="planshet7";
	$addpart='2_';
}

$locals=['en','ru','uk','be','ka','az','hy','kk','uz','de',
				'fr','it','es','pt','pl','bg','sr','el','hu','da',
				'is','lv','lt','et','nb','nl','ro','sq','sk','sl',
				'fi','tr','hr','cs','sv','ms','in','id','iw','he',
				'ca','af','ar','am','th','hi','bn','ja','ko','vi',
				'mk','bs','mn'];
				
$locale=$_POST['locale'];
$number=(int)$_POST['number'];
$base64=$_POST['imgdata'];

if($subdir && strlen($locale) && $number && strlen($base64) && in_array($locale,$locals)) {
	$data = base64_decode(str_replace(' ','+',$base64));
	
	/*
	$path='out/'.$subdir;
	if(!file_exists($path)) {
		mkdir($path);
	}
	$path.='/'.$locale;
	*/
	$path='out/'.$locale;
	
	if(!file_exists($path)) {
		mkdir($path);
	}
	$path.='/'.$addpart.$number.'.png';
	file_put_contents($path, $data);	
}

?>