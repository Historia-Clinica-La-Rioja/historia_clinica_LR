package ar.lamansys.sgh.clinichistory.domain.ips;

import ar.lamansys.sgh.clinichistory.domain.ips.visitor.IpsVisitor;

public interface IpsBo {

    default void accept(IpsVisitor visitor) {
        visitor.visit(this);
    }
}
