Vagrant.configure(2) do |config|
  config.vm.box = "ubuntu/xenial64"

  #
  config.vm.provider "virtualbox" do |vb|
  #   # Display the VirtualBox GUI when booting the machine
  #   vb.gui = true
  #
  #   # Customize the amount of memory on the VM:
  #    vb.memory = "2048"
      vb.memory = "4096"
  end
  #
  # View the documentation for the provider you are using for more
  # information on available options.

  config.vm.provision "shell", inline: <<-SHELL
    # install java8
    sudo apt-get update && apt-get install -y software-properties-common && add-apt-repository ppa:webupd8team/java -y && apt-get update
    sudo echo debconf shared/accepted-oracle-license-v1-1 select true | debconf-set-selections && echo debconf shared/accepted-oracle-license-v1-1 seen true | debconf-set-selections && apt-get install -y oracle-java8-installer
    sudo apt-get install -y make gcc python-pip
    sudo curl -L https://toolbelt.treasuredata.com/sh/install-ubuntu-xenial-td-agent2.sh | sh
    sudo pip install --upgrade pip
    sudo pip install ansible
  SHELL
end
