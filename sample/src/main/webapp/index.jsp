
<html>
<head>
<style>
div.container {
	width: 100%;
	border: 1px solid gray;
}

header, footer {
	padding: 1em;
	color: white;
	background-color: black;
	clear: left;
	text-align: center;
}

nav {
	float: left;
	max-width: 160px;
	margin: 0;
	padding: 1em;
}

nav ul {
	list-style-type: none;
	padding: 0;
}

nav ul a {
	text-decoration: none;
}

article {
	margin-left: 170px;
	/*border-left: 1px solid gray;*/
	padding: 1em;
	overflow: hidden;
}
</style>
</head>
<body>

	<div class="container">
		<header>
			<h1>IFACE</h1>
		</header>
		<article>
		RTSP :
			<div style="margin-top: 20px; width: 430px; background-color: green;">
				<video width="430" height="270" controls>
					<source src="#" type="video/mp4">
					<source src="#" type="video/ogg">
				</video>
			</div>

			<hr>
			<table>
				<tr>
					<th>
					Image RTSP
					<div style="margin-top: 20px; width: 400px; background-color: pink; height: 250px"></div>
					</th>
					<th></th>
					<th><span>Matching confidence</span></th>
					<th></th>
					<th>
					Image Match
					<div style="margin-top: 20px; width: 400px; background-color: pink; height: 250px"></div>
					</th>
				</tr>
			</table>


		</article>
	</div>
</body>
</html>
