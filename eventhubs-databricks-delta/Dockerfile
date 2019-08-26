FROM ubuntu                                                                     
LABEL maintainer="RileyMShea@gmail.com"                                         
                                                                                
RUN apt-get update                                                              
RUN apt-get install sudo                                                        
                                                                                
RUN adduser --disabled-password --gecos '' docker                               
RUN adduser docker sudo                                                         
RUN echo '%sudo ALL=(ALL) NOPASSWD:ALL' >> /etc/sudoers                         
                                                                                
USER docker                                                                     
                                                                                
# this is where I was running into problems with the other approaches           
RUN sudo apt-get update                                                         
                                                                                
RUN sudo apt-get update && sudo apt-get -y install curl bash                    
                                                                                
RUN curl -sL https://aka.ms/InstallAzureCLIDeb | sudo bash                      
RUN sudo apt-get install jq -y                                                  
RUN sudo apt-get install python python-pip -y                                   
RUN pip install --upgrade databricks-cli     
