PLAY_HOME=/home/play-1.2.5
PLAY=$PLAY_HOME/play
APP_SRC=/home/quizapp/quizapp
APP_PROD=/home/quizapp/quizapp_prod

cd $APP_SRC
git pull origin master

cd $APP_PROD
$PLAY stop $APP_PROD

rm -Rf $APP_PROD
mkdir $APP_PROD
cp -R $APP_SRC/* $APP_PROD/
rm $APP_PROD/README.md
rm $APP_PROD/build.sh
cd $APP_PROD
$PLAY deps --sync
$PLAY start
