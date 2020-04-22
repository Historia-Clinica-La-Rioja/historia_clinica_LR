
class SGXPermissions {

    constructor(accountDto) {
        this.authorities = accountDto.authorities;
    }


    hasAnyAuthority(...anyAuthorities) {
        if (anyAuthorities.length === 0) {
            return true;
        }
        let hasAny = anyAuthorities.find(auth => 
            this.authorities.find(userAuthority => userAuthority.authority === auth)
        ) !== undefined;

        return hasAny;
    }

}
  
  export default SGXPermissions;