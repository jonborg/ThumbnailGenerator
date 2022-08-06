userAgent="Mozilla/5.0 (Linux; Android 12) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.5060.71 Mobile Safari/537.36"
url="https://www.deviantart.com/elevenzm/art/01-Mario-Super-Smash-Bros-Ultimate-809554218"
content=$(wget --user-agent="$userAgent" $url -q -O -)
echo $content | grep -Eo "01 Mario - Super Smash Bros. Ultimate[a-zA-Z0-9./?=_-]*" | sort -u
	
